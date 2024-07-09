package com.example.cardValidation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProcessPaymentRequest {
    private String cardNumber;
    private Long amount;
}
