package com.coffeewa.coffeewebapp.accounts;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class account {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    @Pattern(regexp = "^\\d{10}$", message = "phone must contain exactly 10 digits")
    private String phonenumber;

    @NotNull
    @Size(min = 5, message = "must be at least 5 characters long")
    private String username;

    @NotNull
    @Size(min = 8, message = "must be at least 8 characters long")
    private String password;

    
     @JsonIgnore
     private Boolean verified = false;





}
