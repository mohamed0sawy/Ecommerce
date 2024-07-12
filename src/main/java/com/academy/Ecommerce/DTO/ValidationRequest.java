package com.academy.Ecommerce.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ValidationRequest {
    private String number;

    private Long pin;

    private Long cvc;

    private Long expMonth;

    private Long expYear;
}
