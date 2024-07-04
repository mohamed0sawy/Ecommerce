package com.academy.Ecommerce.dto;

import com.academy.Ecommerce.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private User user;
    private String accessToken;
    private String refreshToken;
}
