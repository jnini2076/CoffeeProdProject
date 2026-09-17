package com.coffeewa.coffeewebapp.orders;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemDTO {

    @NotBlank
    private String name;

    @NotNull
    private Double price;
}
