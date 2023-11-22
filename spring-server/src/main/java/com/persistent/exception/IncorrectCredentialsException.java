package com.persistent.exception;

public class IncorrectCredentialsException extends RuntimeException {

    /**
     * Constructs a new IncorrectCredentials from the provided message value.
     * 
     * @param message Detail custom message.
     */
    public IncorrectCredentialsException(String message) {
        super(message);
    }
}