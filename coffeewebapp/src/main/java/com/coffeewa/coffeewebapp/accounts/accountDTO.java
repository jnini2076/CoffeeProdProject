package com.coffeewa.coffeewebapp.accounts;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class accountDTO {

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    @Pattern(regexp = "^\\d{10}$", message = "phone must contain exactly 10 digits")
    private String phonenumber;

    @NotNull
    @NotBlank
    @Size(min = 5, message = "must be at least 5 characters long")
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 8, message = "must be at least 8 characters long")
    private String password;
}
