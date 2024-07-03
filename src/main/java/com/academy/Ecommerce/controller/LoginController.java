package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.DTO.ResetPasswordDTO;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final JavaMailSender javaMailSender;
    private final BCryptPasswordEncoder passwordEncoder;

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

    @GetMapping("/reset")
    public String reset(@RequestParam("token") String token, Model model){
        model.addAttribute("token", token);
        model.addAttribute("resetPasswordDTO", new ResetPasswordDTO());
        return "reset";
    }

    @PostMapping("/reset")
    public String resetPassword(@Valid @ModelAttribute ResetPasswordDTO resetPasswordDTO,
                                BindingResult bindingResult, @RequestParam("token") String token, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("token", token);
            return "reset";
        }

        String password = resetPasswordDTO.getPassword();
        String confirmPassword = resetPasswordDTO.getConfirmPassword();

        if (!password.equals(confirmPassword)) {
            model.addAttribute("token", token);
            model.addAttribute("error", "passwordMismatch");
            return "reset";
        }

        User user = userService.findUserByConfirmationToken(token);
        if (user == null) {
            model.addAttribute("token", token);
            model.addAttribute("error", "userNotFound");
            return "reset";
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setConfirmationToken(null);
        userService.saveUser(user);

        return "redirect:/api/v1/reset?token=" + token + "&success";
    }

}
