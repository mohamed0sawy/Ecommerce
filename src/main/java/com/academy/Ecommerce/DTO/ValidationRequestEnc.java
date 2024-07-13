package com.academy.Ecommerce.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationRequestEnc {
    private String cardNumberEncrypted;

    private String pinEncrypted;

    private String cvcEncrypted;

    private Long expMonth;

    private Long expYear;
}
