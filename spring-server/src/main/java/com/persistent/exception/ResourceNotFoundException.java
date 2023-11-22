/**
 * Custom exception class when a requested resource is not found.
 */
package com.persistent.exception;

public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException from the provided message value.
     * @param message Detail custom message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}