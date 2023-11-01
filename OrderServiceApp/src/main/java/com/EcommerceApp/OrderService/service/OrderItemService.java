package com.EcommerceApp.OrderService.service;

import com.EcommerceApp.OrderService.dto.OrderItemPK;
import com.EcommerceApp.OrderService.dao.OrderItemDao;
import com.EcommerceApp.OrderService.model.OrderItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemDao orderItemDao;

    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemDao.save(orderItem);
    }


    public OrderItem getOrderItemById(Integer orderId, Integer productId) {
        OrderItemPK pk = new OrderItemPK();
        pk.setOrderId(orderId);
        pk.setProductId(productId);
        return orderItemDao.findById(pk).orElse(null);
    }

    public void deleteOrderItem(Integer orderId, Integer productId) {
        OrderItemPK pk = new OrderItemPK();
        pk.setOrderId(orderId);
        pk.setProductId(productId);
        orderItemDao.deleteById(pk);
    }


    public OrderItem updateOrderItem(Integer orderId, Integer productId, OrderItem updatedOrderItem) {
        OrderItemPK pk = new OrderItemPK();
        pk.setOrderId(orderId);
        pk.setProductId(productId);
        Optional<OrderItem> existingOrderItem = orderItemDao.findById(pk);
        if (existingOrderItem.isPresent()) {
            OrderItem orderItem = existingOrderItem.get();
            orderItem.setQuantity(updatedOrderItem.getQuantity());
            orderItem.setItemPrice(updatedOrderItem.getItemPrice());
            return orderItemDao.save(orderItem);
        }

        return null; // Handle not found or other scenarios as needed
    }

    public List<OrderItem> findByOrderId(Integer orderId) {
        return orderItemDao.findByOrderId(orderId);
    }
}
