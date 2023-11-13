package com.EcommerceApp.OrderService.controller;

import com.EcommerceApp.OrderService.dto.OrderItemDTO;
import com.EcommerceApp.OrderService.exception.*;
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
    public ResponseEntity<OrderItemDTO> createOrderItem(@RequestBody OrderItem orderItem) {
        try {
            OrderItemDTO createdOrderItem = orderItemService.createOrderItem(orderItem);
            return new ResponseEntity<>(createdOrderItem, HttpStatus.CREATED);
        } catch (OrderNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getOrderItems(@PathVariable Integer orderId) {
        try {
            List<OrderItemDTO> orderItemDTOS = orderItemService.findByOrderId(orderId);
            return new ResponseEntity<>(orderItemDTOS, HttpStatus.OK);
        }catch (InvalidOrderIdException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{orderId}/{productId}")
    public ResponseEntity<OrderItemDTO> getOrderItem(@PathVariable Integer orderId, @PathVariable Integer productId) {
        try {
            OrderItemDTO orderItemDTO = orderItemService.getOrderItemById(orderId, productId);
            return new ResponseEntity<>(orderItemDTO, HttpStatus.OK);
        }catch (InvalidOrderIdException | InvalidProductIdException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (OrderItemNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // Update an order item
    @PutMapping("/{orderId}/{productId}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(@PathVariable Integer orderId, @PathVariable Integer productId, @RequestBody OrderItem orderItem) {
        try {
            OrderItemDTO updatedOrderItem = orderItemService.updateOrderItem(orderId, productId, orderItem);
            return new ResponseEntity<>(updatedOrderItem, HttpStatus.OK);
        } catch (InvalidOrderIdException | InvalidProductIdException | OrderIdModificationException | ProductIdModificationException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (OrderItemNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e) {
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
}
