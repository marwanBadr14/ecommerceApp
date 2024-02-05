package com.marwan.UserService.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message, Integer id) {
        super(message + ": " + id);
    }

}
