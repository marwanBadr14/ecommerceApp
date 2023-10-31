package com.EcommerceApp.OrderService.controller;

import com.EcommerceApp.OrderService.model.OrderItem;
import com.EcommerceApp.OrderService.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order/items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;


    // Create a new order item
    @PostMapping
    public OrderItem createOrderItem(@RequestBody OrderItem orderItem) {
        return orderItemService.createOrderItem(orderItem);
    }

    @GetMapping("/{orderId}")
    public List<OrderItem> getOrderItems(@PathVariable Integer orderId) {
        return orderItemService.findByOrderId(orderId);
    }

    @GetMapping("/{orderId}/{productId}")
    public OrderItem getOrderItem(@PathVariable Integer orderId, @PathVariable Integer productId) {
        return orderItemService.getOrderItemById(orderId, productId);
    }

    // Update an order item
    @PutMapping("/{orderId}/{productId}")
    public OrderItem updateOrderItem(@PathVariable Integer orderId,@PathVariable Integer productId, @RequestBody OrderItem orderItem) {
        return orderItemService.updateOrderItem(orderId, productId, orderItem);
    }

    // Delete an order item by ID
    @DeleteMapping("/{orderId}/{productId}")
    public void deleteOrderItem(@PathVariable Integer orderId, @PathVariable Integer productId) {
        orderItemService.deleteOrderItem(orderId, productId);
    }
}
