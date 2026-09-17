package com.coffeewa.coffeewebapp.exceptions;

import org.springframework.http.HttpStatus;

public class BusinessRuleViolationException extends ApiException{
     public BusinessRuleViolationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
