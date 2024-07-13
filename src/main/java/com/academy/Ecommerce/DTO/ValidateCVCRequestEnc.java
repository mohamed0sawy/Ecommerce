package com.academy.Ecommerce.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidateCVCRequestEnc {
    private String cardNumberEncrypted;
    private String cvcEncrypted;
}
