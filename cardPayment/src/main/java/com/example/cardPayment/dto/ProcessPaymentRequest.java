package com.example.cardPayment.dto;

import lombok.Data;

@Data
public class ProcessPaymentRequest {
    private String cardNumberEncrypted;
    private Long amount;
}
