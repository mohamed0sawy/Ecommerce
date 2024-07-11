package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.service.RoleService;
import com.academy.Ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RoleService roleService;


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "user-registration";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user, HttpServletRequest request, Model model) {
        User createdUser = userService.createUser(user);

        HttpSession session = request.getSession();
        session.setAttribute("userId", createdUser.getId());
        model.addAttribute("message", "User created successfully!");

        return "success";
    }

}
