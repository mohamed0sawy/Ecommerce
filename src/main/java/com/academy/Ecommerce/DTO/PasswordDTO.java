package com.academy.Ecommerce.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PasswordDTO {
    private String oldPassword;

    @NotNull
    @NotBlank
    @Size(min = 8, message = "password must be at least 8 characters")
    private String newPassword;

    @NotNull
    @NotBlank
    private String confirmPassword;
}
