package com.example.cardValidation.dto;

import lombok.Data;

@Data
public class ValidationRequest {
    private String number;

    private Long pin;

    private Long cvc;

    private Long expMonth;

    private Long expYear;
}
