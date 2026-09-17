package com.coffeewa.coffeewebapp.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends ApiException{
     public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
