package com.coffeewa.coffeewebapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponseDTO {


    private String error ;
    private String message;

}
