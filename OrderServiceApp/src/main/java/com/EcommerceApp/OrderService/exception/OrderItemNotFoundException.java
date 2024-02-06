package com.EcommerceApp.OrderService.exception;

public class OrderItemNotFoundException extends RuntimeException {
    public OrderItemNotFoundException(int id) {
        super("Order Item with id: "+ id +" is not found");
    }
}