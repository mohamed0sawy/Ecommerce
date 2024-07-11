package com.academy.Ecommerce.controller;

//import com.academy.Ecommerce.model.Customer;
import com.academy.Ecommerce.model.User;
//import com.academy.Ecommerce.service.CustomerService;
import com.academy.Ecommerce.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class CustomeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
//    private final CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        User user = userService.findUserByUsername(username);
        user.setLoginTries(0);
        user = userService.saveUser(user);
//        Customer customer = customerService.findCustomerByName(username);
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        response.sendRedirect(request.getContextPath() + "/");
    }
}
