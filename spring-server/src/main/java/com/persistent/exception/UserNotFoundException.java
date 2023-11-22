package com.persistent.exception;

public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException from the provided message value.
     * 
     * @param message Detail custom message.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}