package com.academy.Ecommerce.controller;

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
            response.sendRedirect(request.getContextPath() + "/api/v1/login?locked");
            return;
        }
        String email = request.getParameter("username");
        User user = userService.findUserByEmail(email);
        if (user.getLoginTries() >= 3)
            user.setLocked(true);
        userService.saveUser(user);
        response.sendRedirect(request.getContextPath() + "/api/v1/login?error");
    }
}
