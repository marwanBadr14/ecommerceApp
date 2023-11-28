package com.EcommerceApp.OrderService.service;

import com.EcommerceApp.OrderService.dao.OrderItemDao;
import com.EcommerceApp.OrderService.exception.*;
import com.EcommerceApp.OrderService.feign.InventoryServiceClient;
import com.EcommerceApp.OrderService.mapper.OrderItemMapper;
import com.EcommerceApp.OrderService.mapper.OrderMapper;
import com.EcommerceApp.OrderService.model.OrderItem;

import org.dto.OrderDTO;
import org.dto.OrderItemDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    private final OrderItemDao orderItemDao;
    private final OrderItemMapper orderItemMapper;
    private final OrderService orderService;
    private final InventoryServiceClient inventoryServiceClient;
    private final OrderMapper orderMapper;

    public OrderItemService(OrderItemDao orderItemDao, OrderItemMapper orderItemMapper, OrderService orderService, InventoryServiceClient inventoryServiceClient, OrderMapper orderMapper) {
        this.orderItemDao = orderItemDao;
        this.orderItemMapper = orderItemMapper;
        this.orderService = orderService;
        this.inventoryServiceClient = inventoryServiceClient;
        this.orderMapper = orderMapper;
    }

    public OrderItemDTO createOrderItem(OrderItem orderItem) {
        OrderDTO order = orderService.findById(orderItem.getOrderId());
        orderItem.setItemPrice(inventoryServiceClient.getProductPrice(orderItem.getProductId()).getBody());
        order.setTotalAmount(order.getTotalAmount()
                .add(orderItem.getItemPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))));
        orderService.save(orderMapper.convertToEntity(order));
        inventoryServiceClient.deductFromStock(orderItem.getProductId(),orderItem.getQuantity());
        OrderItem createdOrderItem = orderItemDao.save(orderItem);
        return orderItemMapper.convertToDTO(createdOrderItem);
    }


    public OrderItemDTO getOrderItemById(Integer id) {
        Optional<OrderItem> orderItem = orderItemDao.findById(id);
        if(orderItem.isPresent()){
            return orderItemMapper.convertToDTO(orderItem.get());
        }else {
            throw new OrderItemNotFoundException("Order Item with id: "+ id +" is not found");
        }
    }

    public void deleteOrderItem(Integer id) {
        Optional<OrderItem> orderItem = orderItemDao.findById(id);
        if(orderItem.isPresent()){
            orderItemDao.deleteById(id);
        }else {
            throw new OrderItemNotFoundException("Order Item with id: "+ id +" is not found");
        }
    }


    public OrderItemDTO updateOrderItem(Integer id ,OrderItem updatedOrderItem) {
        Optional<OrderItem> existingOrderItem = orderItemDao.findById(id);
        if (existingOrderItem.isPresent()) {
            OrderItem orderItem = existingOrderItem.get();
            return orderItemMapper.convertToDTO(orderItemDao.save(orderItem));
        }else{
            throw new OrderItemNotFoundException("Order Item with id: "+ id +" is not found");
        }
    }

    public List<OrderItemDTO> findByOrderId(Integer orderId) {
        validatOrderId(orderId);
        List<OrderItem> orderItemList = orderItemDao.findByOrderId(orderId);
        return orderItemList.stream().map(orderItemMapper::convertToDTO).collect(Collectors.toList());
    }

    // TODO: 11/14/2023 move them to a validation class better design
    private void validatOrderId(Integer orderId){
        if(orderId <= 0) throw new InvalidOrderIdException("Order id is Invalid");
    }
    private void validatProductId(Integer productId){
        if(productId <= 0) throw new InvalidOrderIdException("Product id is Invalid");
    }
}