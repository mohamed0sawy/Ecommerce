package com.academy.Ecommerce.util;


import com.academy.Ecommerce.model.User;
import org.springframework.security.core.Authentication;

public class AuthenticationUser {
    private static Authentication authentication;

    public static User get(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
