package com.academy.Ecommerce.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidateCVCRequest {
    private String cardNumber;
    private Long CVC;
}
