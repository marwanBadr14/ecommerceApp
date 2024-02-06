package com.EcommerceApp.OrderService.service;

import com.EcommerceApp.OrderService.dao.OrderItemDao;
import com.EcommerceApp.OrderService.exception.*;
import com.EcommerceApp.OrderService.feign.InventoryServiceClient;
import com.EcommerceApp.OrderService.mapper.OrderItemMapper;
import com.EcommerceApp.OrderService.mapper.OrderMapper;
import com.EcommerceApp.OrderService.model.OrderItem;

import com.EcommerceApp.OrderService.validator.IdValidators;
import org.dto.OrderItemDTO;
import org.springframework.stereotype.Service;

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
    private final IdValidators idValidators;

    public OrderItemService(OrderItemDao orderItemDao, OrderItemMapper orderItemMapper, OrderService orderService, InventoryServiceClient inventoryServiceClient, OrderMapper orderMapper, IdValidators idValidators) {
        this.orderItemDao = orderItemDao;
        this.orderItemMapper = orderItemMapper;
        this.orderService = orderService;
        this.inventoryServiceClient = inventoryServiceClient;
        this.orderMapper = orderMapper;
        this.idValidators = idValidators;
    }



    public OrderItemDTO getOrderItemById(Integer id) {
        Optional<OrderItem> orderItem = orderItemDao.findById(id);
        if(orderItem.isPresent()){
            return orderItemMapper.convertToDTO(orderItem.get());
        }else {
            throw new OrderItemNotFoundException(id);
        }
    }

    public void deleteOrderItem(Integer id) {
        Optional<OrderItem> orderItem = orderItemDao.findById(id);
        if(orderItem.isPresent()){
            orderItemDao.deleteById(id);
        }else {
            throw new OrderItemNotFoundException(id);
        }
    }


    public OrderItemDTO updateOrderItem(Integer id ,OrderItem updatedOrderItem) {
        Optional<OrderItem> existingOrderItem = orderItemDao.findById(id);
        if (existingOrderItem.isPresent()) {
            OrderItem orderItem = existingOrderItem.get();
            return orderItemMapper.convertToDTO(orderItemDao.save(orderItem));
        }else{
            throw new OrderItemNotFoundException(id);
        }
    }

    public List<OrderItemDTO> findByOrderId(Integer orderId) {
        idValidators.validateOrderId(orderId);
        List<OrderItem> orderItemList = orderItemDao.findByOrderId(orderId);
        return orderItemList.stream().map(orderItemMapper::convertToDTO).collect(Collectors.toList());
    }
}