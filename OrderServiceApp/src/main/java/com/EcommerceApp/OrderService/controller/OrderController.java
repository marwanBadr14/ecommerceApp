package com.EcommerceApp.OrderService.controller;

import com.EcommerceApp.OrderService.kafka.OrderProducer;
import com.gizasystems.purchasingservice.dto.PurchaseDTO;
import com.EcommerceApp.OrderService.exception.OrderNotFoundException;
import com.EcommerceApp.OrderService.feign.PurchaseServiceIClient;
import com.EcommerceApp.OrderService.model.Order;
import com.EcommerceApp.OrderService.Status;
import com.EcommerceApp.OrderService.model.OrderItem;
import com.EcommerceApp.OrderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    PurchaseServiceIClient purchaseServiceIClient;
    @Autowired
    OrderProducer orderProducer;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        try {
            order.setOrderDate(LocalDateTime.now());
            order.setOrderStatus(Status.Pending);
            order.setTotalAmount(BigDecimal.ZERO);
            Order createdOrder = orderService.save(order);
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get a list of all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Get a specific order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable int orderId) {
        Optional<Order> order = orderService.findById(orderId);
        if (order.isPresent()) {
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        } else {
            throw new OrderNotFoundException("Order with ID " + orderId + " not found");
        }
    }

    // Update an existing order
    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable int orderId, @RequestBody Order updatedOrder) {
        if (orderService.existsById(orderId)) {
            updatedOrder.setOrderId(orderId);
            Order updated = orderService.save(updatedOrder);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            throw new OrderNotFoundException("Order with ID " + orderId + " not found");
        }
    }

    // Delete an order by ID
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int orderId) {
        if (orderService.existsById(orderId)) {
            orderService.deleteById(orderId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new OrderNotFoundException("Order with ID " + orderId + " not found");
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable int customerId) {
        List<Order> orders = orderService.findByCustomerId(customerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Get orders by order status
    @GetMapping("/status/{orderStatus}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable Status orderStatus) {
        List<Order> orders = orderService.findByOrderStatus(orderStatus);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Get orders with a total amount greater than a specified value
    @GetMapping("/totalAmountGreaterThan/{amount}")
    public ResponseEntity<List<Order>> getOrdersWithTotalAmountGreaterThan(@PathVariable BigDecimal amount) {
        List<Order> orders = orderService.findByTotalAmountGreaterThan(amount);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Get orders within a date range
    @GetMapping("/dateRange")
    public ResponseEntity<List<Order>> getOrdersWithinDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        List<Order> orders = orderService.findByOrderDateBetween(startDate, endDate);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}/execute")
    public ResponseEntity<List<OrderItem>> executeOrder(@PathVariable int orderId) {
        if (orderService.existsById(orderId)) {
            Order order = orderService.findById(orderId).get();
            List<OrderItem> orderItems =  orderService.executeOrder(orderId);
            List<PurchaseDTO> purchaseDTOS = new ArrayList<>();
            for (OrderItem item:orderItems) {
                purchaseDTOS.add(new PurchaseDTO(item.getProductId(), item.getQuantity()));
            }
            //purchaseServiceIClient.processPurchasesRequest(purchaseDTOS);
            orderProducer.sendMessage(order);
            return new ResponseEntity<>(orderItems, HttpStatus.OK);
        } else {
            throw new OrderNotFoundException("Order with ID " + orderId + " not found");
        }
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
