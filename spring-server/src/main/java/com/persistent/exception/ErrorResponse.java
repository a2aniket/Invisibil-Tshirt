/**
 * Class used to generate custom error responses. 
 */
package com.persistent.exception;
import lombok.Data;

@Data
public class ErrorResponse {
    private int status;
    private String message;

    /**
     * Constructs a new empty ErrorResponse.
     */
    public ErrorResponse() {
    }

    /**
     * Constructs a new ErrorResponse from the provided status and message value.
     * @param status The Http Status response code value.
     * @param message The custom exception message.
     */
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

}