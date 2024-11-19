package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle NullPointerException and return a JSON response
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponseDTO> handleNullPointerException(NullPointerException ex) {

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "User is not authenticated",
                ex.getMessage()
                // HttpStatus.BAD_REQUEST.value()
        );

        // TODO - Print ResponseEntity

        // Return the error response with HTTP status BAD_REQUEST
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // You can handle other exceptions in a similar way
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("Unexpected error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

