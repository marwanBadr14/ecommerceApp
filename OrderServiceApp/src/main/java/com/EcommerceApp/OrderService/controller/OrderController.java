package com.EcommerceApp.OrderService.controller;

import com.EcommerceApp.OrderService.model.Order;
import com.EcommerceApp.OrderService.Status;
import com.EcommerceApp.OrderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(Status.Pending);
        order.setTotalAmount(BigDecimal.ZERO);
        return orderService.save(order);
    }

    // Get a list of all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }

    // Get a specific order by ID
    @GetMapping("/{orderId}")
    public Optional<Order> getOrderById(@PathVariable int orderId) {
        return orderService.findById(orderId);
    }

    // Update an existing order
    @PutMapping("/{orderId}")
    public Order updateOrder(@PathVariable int orderId, @RequestBody Order updatedOrder) {
        updatedOrder.setOrderId(orderId); // Ensure the ID is set to the correct value
        return orderService.save(updatedOrder);
    }

    // Delete an order by ID
    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable int orderId) {
        orderService.deleteById(orderId);
    }

    @GetMapping("/customer/{customerId}")
    public List<Order> getOrdersByCustomerId(@PathVariable int customerId) {
        return orderService.findByCustomerId(customerId);
    }

    // Get orders by order status
    @GetMapping("/status/{orderStatus}")
    public List<Order> getOrdersByStatus(@PathVariable Status orderStatus) {
        return orderService.findByOrderStatus(orderStatus);
    }

    // Get orders with a total amount greater than a specified value
    @GetMapping("/totalAmountGreaterThan/{amount}")
    public List<Order> getOrdersWithTotalAmountGreaterThan(@PathVariable BigDecimal amount) {
        return orderService.findByTotalAmountGreaterThan(amount);
    }

    // Get orders within a date range
    @GetMapping("/dateRange")
    public List<Order> getOrdersWithinDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        return orderService.findByOrderDateBetween(startDate, endDate);
    }

}
