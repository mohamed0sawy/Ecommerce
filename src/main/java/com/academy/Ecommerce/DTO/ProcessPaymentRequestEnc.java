package com.academy.Ecommerce.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProcessPaymentRequestEnc {
    private String cardNumberEncrypted;
    private Long amount;
}
