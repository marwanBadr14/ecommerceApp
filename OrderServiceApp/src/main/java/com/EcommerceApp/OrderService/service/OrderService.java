package com.EcommerceApp.OrderService.service;

import com.EcommerceApp.OrderService.dao.OrderItemDao;
import com.EcommerceApp.OrderService.exception.*;
import com.EcommerceApp.OrderService.feign.InventoryServiceClient;
import com.EcommerceApp.OrderService.feign.PurchaseServiceIClient;
import com.EcommerceApp.OrderService.kafka.OrderProducer;
import com.EcommerceApp.OrderService.mapper.OrderItemMapper;
import com.EcommerceApp.OrderService.mapper.OrderMapper;
import com.EcommerceApp.OrderService.model.Order;
import com.EcommerceApp.OrderService.Status;
import com.EcommerceApp.OrderService.dao.OrderDao;
import com.EcommerceApp.OrderService.validator.IdValidators;
import org.dto.OrderDTO;
import org.dto.OrderItemDTO;
import org.dto.PurchaseDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    OrderDao orderDao;
    OrderItemDao orderItemDao;
    InventoryServiceClient inventoryServiceClient;
    OrderMapper orderMapper;
    PurchaseServiceIClient purchaseServiceIClient;
    OrderProducer orderProducer;
    OrderItemMapper orderItemMapper;
    IdValidators idValidators;

    public OrderService(OrderDao orderDao, OrderItemDao orderItemDao, InventoryServiceClient inventoryServiceClient, OrderMapper orderMapper, PurchaseServiceIClient purchaseServiceIClient, OrderProducer orderProducer, OrderItemMapper orderItemMapper, IdValidators idValidators) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.inventoryServiceClient = inventoryServiceClient;
        this.orderMapper = orderMapper;
        this.purchaseServiceIClient = purchaseServiceIClient;
        this.orderProducer = orderProducer;
        this.orderItemMapper = orderItemMapper;
        this.idValidators = idValidators;
    }

    public OrderDTO save(Order order) {
        return orderMapper.convertToDTO(orderDao.save(order));
    }

    public List<OrderDTO> findAll() {
        return orderDao.findAll().stream().map(orderMapper::convertToDTO).collect(Collectors.toList());
    }

    public OrderDTO findById(int orderId) {
        idValidators.validateOrderId(orderId);
        Optional<Order> order = orderDao.findById(orderId);
        if(order.isPresent()) {
            return orderMapper.convertToDTO(order.get());
        }else {
            throw new OrderNotFoundException(orderId);
        }
    }

    public void deleteById(int orderId) {
        idValidators.validateOrderId(orderId);
        if(existsById(orderId)) {
            orderDao.deleteById(orderId);
        }else{
            throw new OrderNotFoundException(orderId);
        }
    }

    public List<OrderDTO> findByCustomerId(int customerId) {
        idValidators.validateCustomerId(customerId);
        return orderDao.findByCustomerId(customerId).stream().map(orderMapper::convertToDTO).collect(Collectors.toList());
    }

    public List<OrderDTO> findByTotalAmountGreaterThan(BigDecimal amount) {
        return orderDao.findByTotalAmountGreaterThan(amount).stream().map(orderMapper::convertToDTO).collect(Collectors.toList());
    }

    public List<OrderDTO> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate.isAfter(endDate)){
            throw new InvalidDateRangeException();
        }
        return orderDao.findByOrderDateBetween(startDate,endDate).stream().map(orderMapper::convertToDTO).collect(Collectors.toList());
    }

    public boolean existsById(int orderId) {
        return orderDao.existsById(orderId);
    }




    public OrderDTO update(int orderId, Order updatedOrder) {
        if(!existsById(orderId))
            throw new OrderNotFoundException(orderId);

        OrderDTO orderDTO = findById(orderId);
        Order order = orderMapper.convertToEntity(orderDTO);
        if(updatedOrder.getOrderItems()!=null)
            order.setOrderItems(updatedOrder.getOrderItems());
        if(updatedOrder.getTotalAmount()!=null)
            order.setTotalAmount(updatedOrder.getTotalAmount());
        save(order);
        return orderMapper.convertToDTO(order);
    }

    public List<OrderItemDTO> submitOrder(List<OrderItemDTO> orderItemDTOS){
        Integer orderId = orderItemDTOS.get(0).getOrderId();
        if(!existsById(orderId))
            throw new OrderNotFoundException(orderId);

        OrderDTO orderDto = findById(orderId);
        List<OrderItemDTO> purchasedProducts = new ArrayList<>();
        List<PurchaseDTO> purchaseDTOS = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.valueOf(0.0);
        for (OrderItemDTO orderItemDTO : orderItemDTOS) {
            if (Boolean.TRUE.equals((inventoryServiceClient.deductFromStock(orderItemDTO.getProductId(), orderItemDTO.getQuantity())).getBody())) {
                purchasedProducts.add(orderItemDTO);
                orderItemDTO.setItemPrice(inventoryServiceClient.getProductPrice(orderItemDTO.getProductId()).getBody());
                purchaseDTOS.add(new PurchaseDTO(orderItemDTO.getProductId(), orderItemDTO.getQuantity()));
                totalAmount = totalAmount.add(orderItemDTO.getItemPrice().multiply(BigDecimal.valueOf(orderItemDTO.getQuantity())));
            }
        }

        orderDto.setOrderItems(purchasedProducts);
        orderDto.setTotalAmount(totalAmount);
        orderDao.save(orderMapper.convertToEntity(orderDto));


        purchaseServiceIClient.processPurchasesRequest(purchaseDTOS);
        orderProducer.sendMessage(orderDto);

        return purchasedProducts;

        }



}