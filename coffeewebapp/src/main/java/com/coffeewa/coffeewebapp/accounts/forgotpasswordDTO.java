package com.coffeewa.coffeewebapp.accounts;


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
public class forgotpasswordDTO {

    @NotNull
    private String username;


    private int verificationCode;

    @NotNull
    @Size(min = 8, message = "must be at least 8 characters long")
    private String newpassword;

}
