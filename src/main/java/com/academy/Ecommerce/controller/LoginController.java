package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final JavaMailSender javaMailSender;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/forgetPassword")
    public String forgetPassword(){
        return "forgetPassword";
    }

    @PostMapping("/forgetPassword")
    public String forgetPasswordEmail(@RequestParam("email") String email, Model model){
        User user = userService.findUserByEmail(email);
        if (user == null){
            return "redirect:/api/v1/forgetPassword?userNotFound";
        }
        String confirmationToken = UUID.randomUUID().toString();
        user.setConfirmationToken(confirmationToken);
        // save the user to DB
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Reset Password");
        message.setText("Hello " + user.getUsername() + ",\n" +
                "To reset your password, please click the link below : \n" +
                "http://localhost:8080/api/v1/reset?token="+user.getConfirmationToken());
        javaMailSender.send(message);
        userService.saveUser(user);
        return "redirect:/api/v1/forgetPassword?emailSent";
    }
}
