package com.example.cardValidation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidateCVCRequest {
    private String cardNumberEncrypted;
    private String cvcEncrypted;
}
