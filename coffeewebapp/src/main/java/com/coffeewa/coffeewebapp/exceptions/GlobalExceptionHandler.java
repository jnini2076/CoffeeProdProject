package com.coffeewa.coffeewebapp.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.coffeewa.coffeewebapp.dtos.ErrorResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDTO> handleApiException(ApiException ex) {
        return ResponseEntity
            .status(ex.getStatus())
            .body(new ErrorResponseDTO(ex.getStatus().getReasonPhrase(), ex.getMessage()));
    }
}
