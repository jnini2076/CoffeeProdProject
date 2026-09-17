package com.coffeewa.coffeewebapp.accounts;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter
@Setter


public class verifydto {

    @NotBlank
    private String username;
    @NotBlank
    private int verificationCode;
}
