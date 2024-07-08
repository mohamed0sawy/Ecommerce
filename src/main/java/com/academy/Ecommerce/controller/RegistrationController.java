package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.DTO.RegisterDTO;
import com.academy.Ecommerce.model.Role;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.service.RoleService;
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

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute RegisterDTO registerDTO,
                               BindingResult bindingResult, Model model) {
        User existedUser = userService.findUserByEmail(registerDTO.getEmail());
        if (existedUser != null) {
            return "redirect:/api/v1/register?userFound";
        }
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        String password = registerDTO.getPassword();
        String confirmPassword = registerDTO.getConfirmPassword();
        if (!password.equals(confirmPassword)) {
            model.addAttribute("registerDTO", new RegisterDTO());
            model.addAttribute("error", "passwordMismatch");
            return "registration";
        }
        User user = createUserFromDTO(registerDTO);
        userService.saveUser(user);
        sendConfirmationEmail(user);

        return "redirect:/api/v1/register?success";
    }

    private User createUserFromDTO(RegisterDTO registerDTO) {
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setLocked(false);
        user.setEnabled(false);
        String token = UUID.randomUUID().toString();
        user.setConfirmationToken(token);
        Role role = roleService.findRoleByName("customer");
        user.setRoles(List.of(role));
        return user;
    }

    private void sendConfirmationEmail(User user) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = String.format(
                    "<p>Hello %s,</p>" +
                            "<p>To activate your account, please click the link below:</p>" +
                            "<p><a href='http://localhost:8080/api/v1/activate?token=%s'>Activate Account</a></p>",
                    user.getUsername(), user.getConfirmationToken());

            helper.setText(htmlMsg, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Confirm Account");

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e){
            System.out.println(e.getMessage());
        }
    }

    @GetMapping("/activate")
    public String activate(@RequestParam("token") String token, Model model) {
        User user = userService.findUserByConfirmationToken(token);
        if (user == null) {
            model.addAttribute("activate", "expired");
            return "activate";
        }
        user.setConfirmationToken(null);
        user.setEnabled(true);
        userService.saveUser(user);
        model.addAttribute("activate", "success");
        return "activate";
    }
//
//    @GetMapping("/del")
//    public void del() {
////        User user = userService.findUserByEmail("motarek778899@gmail.com");
//        User user = userService.findUserByEmail("sawy@mail.com");
//        userService.deleteUser(user);
//    }

}
