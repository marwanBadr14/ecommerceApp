package com.gizasystems.inventoryservice.exception;

public class QuantityExceededException extends RuntimeException{
    public QuantityExceededException(String message) {
        super(message);
    }
}
