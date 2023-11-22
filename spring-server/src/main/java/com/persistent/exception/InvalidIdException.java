/**
 * Custom exception class when invalid id is provided.
 */
package com.persistent.exception;

public class InvalidIdException extends RuntimeException {

    /**
     * Constructs a new InvalidIdException from the provided message value.
     * @param message Detail custom message.
     */
    public InvalidIdException(String message) {
        super(message);
    }
}
