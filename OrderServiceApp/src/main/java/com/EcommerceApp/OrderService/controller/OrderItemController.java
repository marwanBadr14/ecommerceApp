package com.EcommerceApp.OrderService.controller;

import com.EcommerceApp.OrderService.exception.InvalidOrderIdException;
import com.EcommerceApp.OrderService.exception.InvalidProductIdException;
import com.EcommerceApp.OrderService.exception.OrderNotFoundException;
import com.EcommerceApp.OrderService.exception.OrderItemNotFoundException;
import com.EcommerceApp.OrderService.feign.InventoryServiceClient;
import com.EcommerceApp.OrderService.model.Order;
import com.EcommerceApp.OrderService.model.OrderItem;
import com.EcommerceApp.OrderService.service.OrderItemService;
import com.EcommerceApp.OrderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/order/items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    InventoryServiceClient inventoryServiceClient;

    // Create a new order item

    @PostMapping
    public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItem orderItem) {
        try {
            Order order = orderService.findById(orderItem.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderItem.getOrderId() + " not found"));
            ResponseEntity<BigDecimal> productPriceResponse = inventoryServiceClient.getProductPrice(orderItem.getProductId());
            if (productPriceResponse.getStatusCode() != HttpStatus.OK || productPriceResponse.getBody() == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            BigDecimal productPrice = productPriceResponse.getBody();
            BigDecimal itemTotalPrice = productPrice.multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            order.setTotalAmount(order.getTotalAmount().add(itemTotalPrice));
            ResponseEntity<Integer> deductionResponse = inventoryServiceClient.deductFromStock(orderItem.getProductId(), orderItem.getQuantity());
            if (deductionResponse.getStatusCode() != HttpStatus.NO_CONTENT) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            OrderItem createdOrderItem = orderItemService.createOrderItem(orderItem);
            return new ResponseEntity<>(createdOrderItem, HttpStatus.CREATED);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable Integer orderId) {
        if (orderId <= 0) {
            throw new InvalidOrderIdException("Invalid order Id: " + orderId);
        }
        List<OrderItem> orderItems = orderItemService.findByOrderId(orderId);
        if (orderItems.isEmpty()) {
            throw new OrderItemNotFoundException("Order items not found for orderId: " + orderId);
        }
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }


    @GetMapping("/{orderId}/{productId}")
    public ResponseEntity<OrderItem> getOrderItem(@PathVariable Integer orderId, @PathVariable Integer productId) {
        if (orderId <= 0) {
            throw new InvalidOrderIdException("Invalid order Id: " + orderId);
        }
        if (productId <= 0) {
            throw new InvalidProductIdException("Invalid product Id: " + productId);
        }
        OrderItem orderItem = orderItemService.getOrderItemById(orderId, productId);
        if (orderItem == null) {
            throw new OrderItemNotFoundException("Order item with Order ID " + orderId + " and Product ID " + productId + " not found");
        }
        return new ResponseEntity<>(orderItem, HttpStatus.OK);
    }

    // Update an order item
    @PutMapping("/{orderId}/{productId}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable Integer orderId, @PathVariable Integer productId, @RequestBody OrderItem orderItem) {
        try {
            OrderItem updatedOrderItem = orderItemService.updateOrderItem(orderId, productId, orderItem);
            return new ResponseEntity<>(updatedOrderItem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete an order item by ID
    @DeleteMapping("/{orderId}/{productId}")
    public ResponseEntity<String> deleteOrderItem(@PathVariable Integer orderId, @PathVariable Integer productId) {
        try {
            orderItemService.deleteOrderItem(orderId, productId);
            return new ResponseEntity<>("Order item deleted successfully", HttpStatus.NO_CONTENT);
        } catch (OrderItemNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the order item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderItemNotFoundException.class)
    public ResponseEntity<String> handleOrderItemNotFoundException(OrderItemNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidOrderIdException.class)
    public ResponseEntity<String> handleInvalidOrderIdException(InvalidOrderIdException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidProductIdException.class)
    public ResponseEntity<String> handleInvalidProductIdException(InvalidProductIdException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
