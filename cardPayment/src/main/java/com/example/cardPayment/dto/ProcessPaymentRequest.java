package com.example.cardPayment.dto;

import lombok.Data;

@Data
public class ProcessPaymentRequest {
    private String cardNumber;
    private Long amount;
}
