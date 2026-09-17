package com.coffeewa.coffeewebapp.orders;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderPaymentDTO {

    @NotBlank
    private String customerName;

    @NotNull
    @NotEmpty
    @Valid
    private List<CartItemDTO> items;

    @NotNull
    private Double totalAmount;

    @NotBlank
    @Pattern(regexp = "1234 1234 1234 1234", message = "Card number must be 1234 1234 1234 1234 (demo only)")
    private String cardNumber;

    @NotBlank
    private String cardHolderName;

    @NotBlank
    private String expiryDate;

    @NotBlank
    @Size(min = 3, max = 3, message = "CVV must be 3 digits")
    private String cvv;
}
