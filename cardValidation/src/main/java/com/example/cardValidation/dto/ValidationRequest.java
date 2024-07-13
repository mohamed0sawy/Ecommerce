package com.example.cardValidation.dto;

import lombok.Data;

@Data
public class ValidationRequest {
    private String cardNumberEncrypted;

    private String pinEncrypted;

    private String cvcEncrypted;

    private Long expMonth;

    private Long expYear;
}
