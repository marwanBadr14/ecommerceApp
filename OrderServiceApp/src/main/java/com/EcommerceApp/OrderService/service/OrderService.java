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

    public OrderService(OrderDao orderDao, OrderItemDao orderItemDao, InventoryServiceClient inventoryServiceClient, OrderMapper orderMapper, PurchaseServiceIClient purchaseServiceIClient, OrderProducer orderProducer, OrderItemMapper orderItemMapper) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.inventoryServiceClient = inventoryServiceClient;
        this.orderMapper = orderMapper;
        this.purchaseServiceIClient = purchaseServiceIClient;
        this.orderProducer = orderProducer;
        this.orderItemMapper = orderItemMapper;
    }

    public OrderDTO save(Order order) {
        return orderMapper.convertToDTO(orderDao.save(order));
    }

    public List<OrderDTO> findAll() {
        return orderDao.findAll().stream().map(orderMapper::convertToDTO).collect(Collectors.toList());
    }

    public OrderDTO findById(int orderId) {
        validateOrderId(orderId);
        Optional<Order> order = orderDao.findById(orderId);
        if(order.isPresent()) {
            return orderMapper.convertToDTO(order.get());
        }else {
            throw new OrderNotFoundException("Order with order id: "+ orderId+" not found");
        }
    }

    public void deleteById(int orderId) {
        validateOrderId(orderId);
        if(existsById(orderId)) {
            orderDao.deleteById(orderId);
        }else{
            throw new OrderNotFoundException("Order with order id: "+ orderId+" is not found");
        }
    }

    public List<OrderDTO> findByCustomerId(int customerId) {
        validateCustomerId(customerId);
        return orderDao.findByCustomerId(customerId).stream().map(orderMapper::convertToDTO).collect(Collectors.toList());
    }

//    public List<OrderDTO> findByOrderStatus(Status orderStatus) {
//        return orderDao.findByOrderStatus(orderStatus).stream().map(orderMapper::convertToDTO).collect(Collectors.toList());
//    }

    public List<OrderDTO> findByTotalAmountGreaterThan(BigDecimal amount) {
        return orderDao.findByTotalAmountGreaterThan(amount).stream().map(orderMapper::convertToDTO).collect(Collectors.toList());
    }

    public List<OrderDTO> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate.isAfter(endDate)){
            throw new InvalidDateRangeException("Invalid date range: Start date must be on or before the end date.");
        }
        return orderDao.findByOrderDateBetween(startDate,endDate).stream().map(orderMapper::convertToDTO).collect(Collectors.toList());
    }

    public boolean existsById(int orderId) {
        return orderDao.existsById(orderId);
    }

    private void validateOrderId(Integer orderId){
        if(orderId <= 0) throw new InvalidOrderIdException("Order id is Invalid");
    }

    private void validateCustomerId(Integer customerId){
        if(customerId <= 0) throw new InvalidCustomerIdException("Customer id is Invalid");
    }


    public OrderDTO update(int orderId, Order updatedOrder) {
        OrderDTO orderDTO = findById(orderId);
        Order order = orderMapper.convertToEntity(orderDTO);
//        if(updatedOrder.getOrderStatus()!=null)order.setOrderStatus(updatedOrder.getOrderStatus());
        if(updatedOrder.getOrderItems()!=null)order.setOrderItems(updatedOrder.getOrderItems());
        if(updatedOrder.getTotalAmount()!=null)order.setTotalAmount(updatedOrder.getTotalAmount());
        save(order);
        return orderMapper.convertToDTO(order);
    }

    public List<OrderItemDTO> submitOrder(List<OrderItemDTO> orderItemDTOS){
        Integer orderId = orderItemDTOS.get(0).getOrderId();
        if(existsById(orderId)) {
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

        throw new OrderNotFoundException("Order with ID " + orderId + " not found");

    }



}