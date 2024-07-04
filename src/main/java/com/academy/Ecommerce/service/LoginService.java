package com.academy.Ecommerce.service;

import com.academy.Ecommerce.dto.LoginRequest;
import com.academy.Ecommerce.dto.LoginResponse;

public interface LoginService {
    public LoginResponse login(LoginRequest loginRequest);
}
