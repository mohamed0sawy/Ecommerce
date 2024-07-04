package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.exception.UserIsNotEnabledException;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final UserService userService;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof InternalAuthenticationServiceException) {
            handleInternalAuthenticationServiceException(request, response, exception);
            return;
        }

        handleFailedLoginAttempt(request);

        response.sendRedirect(request.getContextPath() + "/api/v1/login?error");
    }

    private void handleInternalAuthenticationServiceException(HttpServletRequest request, HttpServletResponse response,
                                                              AuthenticationException exception) throws IOException {
        String message = exception.getMessage();
        String redirectUrl = null;

        if ("User account is locked".equals(message)) {
            redirectUrl = "/api/v1/login?locked";
        } else if ("user is not enabled".equals(message)) {
            redirectUrl = "/api/v1/login?notEnabled";
        }

        if (redirectUrl != null) {
            response.sendRedirect(request.getContextPath() + redirectUrl);
        }
    }

    private void handleFailedLoginAttempt(HttpServletRequest request) {
        String email = request.getParameter("username");
        User user = userService.findUserByEmail(email);

        if (user != null) {
            if (user.getLoginTries() >= 3) {
                user.setLocked(true);
            }
            userService.saveUser(user);
        }
    }
}
