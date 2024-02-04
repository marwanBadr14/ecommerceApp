package com.marwan.UserService.exceptions;

public class WrongCredentialException extends RuntimeException {

    public WrongCredentialException(String message) {
        super(message);
    }
}