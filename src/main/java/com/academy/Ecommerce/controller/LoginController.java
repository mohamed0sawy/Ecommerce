package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.DTO.ResetPasswordDTO;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
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
    public String forgetPasswordEmail(@RequestParam("email") String email, Model model) {
        User user = userService.findUserByEmail(email);

        if (user == null) {
            return "redirect:/api/v1/forgetPassword?userNotFound";
        }

        sendPasswordResetEmail(user);
        userService.saveUser(user);

        return "redirect:/api/v1/forgetPassword?emailSent";
    }

    private void sendPasswordResetEmail(User user) {
        String confirmationToken = UUID.randomUUID().toString();
        user.setConfirmationToken(confirmationToken);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = buildPasswordResetEmailContent(user);
            helper.setText(htmlMsg, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Reset Password");

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String buildPasswordResetEmailContent(User user) {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            System.out.println("Error loading application.properties: " + e.getMessage());
            return null;
        }

        String serverPort = properties.getProperty("server.port");
        String baseUrl = "http://localhost:" + serverPort + "/api/v1/reset";
        String link = String.format(baseUrl + "?token=%s", user.getConfirmationToken());

        return String.format("<p>Hello %s,</p>" +
                        "<p>To reset your password, please click the link below:</p>" +
                        "<p><a href='%s'>Reset Password</a></p>",
                user.getUsername(), link);
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
        updateUserPassword(user, resetPasswordDTO.getPassword());
        return "redirect:/api/v1/reset?token=" + token + "&success";
    }
    private void updateUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        user.setConfirmationToken(null);
        user.setLocked(false);
        user.setLoginTries(0);
        userService.saveUser(user);
    }
}
