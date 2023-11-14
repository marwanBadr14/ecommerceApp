package com.EcommerceApp.OrderService.exception;

public class InvalidOrderIdException extends RuntimeException {
    public InvalidOrderIdException(String s) {
        super(s);
    }
}
