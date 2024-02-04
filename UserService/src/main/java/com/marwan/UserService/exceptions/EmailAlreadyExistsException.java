package com.marwan.UserService.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message, String email) {
        super(message + ": " + email);
    }
}