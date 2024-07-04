package com.academy.Ecommerce.service;

import com.academy.Ecommerce.dto.SignupRequest;
import com.academy.Ecommerce.model.User;
import org.springframework.stereotype.Service;

@Service
public interface SignupService {
    public User signup(SignupRequest signupRequest);
}
