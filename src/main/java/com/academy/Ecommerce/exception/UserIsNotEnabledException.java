package com.academy.Ecommerce.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

public class UserIsNotEnabledException extends AuthenticationServiceException {

    public UserIsNotEnabledException(String message){
        super(message);
    }
}
