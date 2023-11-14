package com.gizasystems.inventoryservice.exception;

public class ProductNotFoundException extends RuntimeException
{
    public ProductNotFoundException(String message) {
        super(message);
    }
}
