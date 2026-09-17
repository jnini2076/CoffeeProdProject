package com.coffeewa.coffeewebapp.accounts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class passworddto {



    @NotNull
    @NotBlank
    @Size(min = 5, message = "must be at least 5 characters long")
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 8, message = "must be at least 8 characters long")
    private String password;



}
