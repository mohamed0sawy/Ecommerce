package com.academy.Ecommerce.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class cardInfoDTO {
    @NotBlank(message = "can't be blank")
    @NotNull(message = "please enter a value")
    @Size(min = 16, max = 16, message = "card number should be 16 digits")
    private String cardNumber;
    @NotBlank(message = "can't be blank")
    private String month;
    @NotBlank(message = "can't be blank")
    private String year;
    @NotBlank(message = "can't be blank")
    @NotNull(message = "please enter a value")
    @Size(min = 3, max = 3, message = "cvv should be 3 digits")
    private String cvv;
    private double totalPrice;
}
