/**
 * Global exception handler class
 */
package com.persistent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Used to handle an exception that is thrown when a requested resource is not found.
     * @param ex The exception that was thrown.
     * @return ResponseEntity that contains a custom error message.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException ex) {
        String message = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), message);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Used to handle an exception that is thrown when entered id is invalid. 
     * @param ex The exception that was thrown.
     * @return ResponseEntity that contains a custom error message.
     */
    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidIdException ex) {
        String message = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Used to handle an exception that is thrown when entered credentials are
     * incorrect.
     * 
     * @param ex The exception that was thrown.
     * @return ResponseEntity that contains a custom error message.
     */
    @ExceptionHandler(IncorrectCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(IncorrectCredentialsException ex) {
        String message = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Used to handle an exception that is thrown when a user is not found.
     * 
     * @param ex The exception that was thrown.
     * @return ResponseEntity that contains a custom error message.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(UserNotFoundException ex) {
        String message = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), message);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.FORBIDDEN);
    }
}
