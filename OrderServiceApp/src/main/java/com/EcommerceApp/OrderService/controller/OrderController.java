package com.EcommerceApp.OrderService.controller;

import com.EcommerceApp.OrderService.exception.InvalidCustomerIdException;
import com.EcommerceApp.OrderService.exception.InvalidDateRangeException;
import com.EcommerceApp.OrderService.exception.InvalidOrderIdException;
import com.EcommerceApp.OrderService.kafka.OrderProducer;
import com.EcommerceApp.OrderService.exception.OrderNotFoundException;
import com.EcommerceApp.OrderService.feign.PurchaseServiceIClient;
import com.EcommerceApp.OrderService.model.Order;
import com.EcommerceApp.OrderService.Status;
import com.EcommerceApp.OrderService.service.OrderService;
import jakarta.ws.rs.POST;
import org.dto.OrderDTO;
import org.dto.OrderItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    OrderService orderService;
    PurchaseServiceIClient purchaseServiceIClient;
    OrderProducer orderProducer;

    public OrderController(OrderService orderService, PurchaseServiceIClient purchaseServiceIClient, OrderProducer orderProducer) {
        this.orderService = orderService;
        this.purchaseServiceIClient = purchaseServiceIClient;
        this.orderProducer = orderProducer;
    }

    @PostMapping("/create")
    // TODO: 11/14/2023 dont take entity as input
    public ResponseEntity<OrderDTO> createOrder(@RequestBody Order order) {
        try {
            OrderDTO createdOrder = orderService.save(order);
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get a list of all orders
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        try {
            List<OrderDTO> orders = orderService.findAll();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get a specific order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable int orderId) {
        try{
            OrderDTO order = orderService.findById(orderId);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (InvalidOrderIdException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (OrderNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an existing order
    @PutMapping("/{orderId}")
    // TODO: 11/14/2023 dont take entity as input
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable int orderId, @RequestBody Order updatedOrder) {
        try{
            OrderDTO updated = orderService.update(orderId,updatedOrder);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }
        catch (InvalidOrderIdException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (OrderNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // Delete an order by ID
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int orderId) {
        try {
            orderService.deleteById(orderId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (OrderNotFoundException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomerId(@PathVariable int customerId) {
        try {
            List<OrderDTO> orders = orderService.findByCustomerId(customerId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }catch (InvalidCustomerIdException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get orders by order status
    @GetMapping("/status/{orderStatus}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable Status orderStatus) {
        try {
            List<OrderDTO> orders = orderService.findByOrderStatus(orderStatus);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get orders with a total amount greater than a specified value
    @GetMapping("/totalAmountGreaterThan/{amount}")
    public ResponseEntity<List<OrderDTO>> getOrdersWithTotalAmountGreaterThan(@PathVariable BigDecimal amount) {
        try {
            List<OrderDTO> orders = orderService.findByTotalAmountGreaterThan(amount);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get orders within a date range
//    @GetMapping("/dateRange")
//    public ResponseEntity<List<OrderDTO>> getOrdersWithinDateRange(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate
//    ) {
//        try {
//            List<OrderDTO> orders = orderService.findByOrderDateBetween(startDate, endDate);
//            return new ResponseEntity<>(orders, HttpStatus.OK);
//        }catch (InvalidDateRangeException e){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        catch (Exception e){
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/{orderId}/execute")
    public ResponseEntity<List<OrderItemDTO>> executeOrder(@PathVariable int orderId) {
        try{
            List<OrderItemDTO> orderItemDTOS =  orderService.executeOrder(orderId);
            return new ResponseEntity<>(orderItemDTOS, HttpStatus.OK);
        }catch (InvalidOrderIdException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (OrderNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/submit")
    public ResponseEntity<List<OrderItemDTO>> submitOrder(@RequestBody List<OrderItemDTO> orderItemDTOS){
        return new ResponseEntity<>(orderService.submitOrder(orderItemDTOS), HttpStatus.OK);
    }

}