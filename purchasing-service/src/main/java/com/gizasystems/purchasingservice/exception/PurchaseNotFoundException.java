package com.gizasystems.purchasingservice.exception;

public class PurchaseNotFoundException extends RuntimeException {
    public PurchaseNotFoundException() {
        super("Purchase not found");
    }
    public PurchaseNotFoundException(String message) {
        super(message);
    }
}
