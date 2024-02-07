package com.EcommerceApp.OrderService.exception;

public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException() {
        super("Invalid date range: Start date must be on or before the end date.");
    }
}
