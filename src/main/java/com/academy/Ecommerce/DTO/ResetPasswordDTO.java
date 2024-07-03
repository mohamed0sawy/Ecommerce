package com.academy.Ecommerce.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResetPasswordDTO {

//    @NotBlank(message = "Password is mandatory")
//    @Size(min = 8, message = "Password must be at least 8 characters long")
//    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[^!@#$%^&*+-\\\\]{8,}$",
//            message = "Password must contain at least one letter and no special characters like +, -, %, &, $, #")
    private String password;

//    @NotBlank(message = "Confirm Password is mandatory")
    private String confirmPassword;

}
