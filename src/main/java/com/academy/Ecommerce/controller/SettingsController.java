package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.DTO.EmailDTO;
import com.academy.Ecommerce.DTO.PasswordDTO;
import com.academy.Ecommerce.DTO.UserNameDTO;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/profile/settings")
@RequiredArgsConstructor
public class SettingsController {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public String settings(){
        return "settings";
    }
    @GetMapping("/name")
    public String name(HttpServletRequest request, Model model){
        User user = getUserFromSession(request);
        UserNameDTO userNameDTO = new UserNameDTO();
        userNameDTO.setUserName(user.getUsername());
        model.addAttribute("userNameDTO", userNameDTO);
        return "name";
    }

    @PostMapping("/name")
    public String nameUpdate(@Valid @ModelAttribute UserNameDTO userNameDTO,
                             BindingResult bindingResult, HttpServletRequest request, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("userNameDTO", userNameDTO);
            return "name";
        }
        String newUserName = userNameDTO.getUserName().trim();
        HttpSession session = request.getSession();
        User existingUser = (User) session.getAttribute("user");
        User updatedUser = userService.findUserByEmail(existingUser.getEmail());
        updatedUser.setUsername(newUserName);
        userService.saveUser(updatedUser);
        session.setAttribute("user", updatedUser);
        return "redirect:/api/v1/profile/settings/name?success";
    }
    @GetMapping("/email")
    public String email(HttpServletRequest request, Model model){
        User user = getUserFromSession(request);
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setEmail(user.getEmail());
        model.addAttribute("emailDTO", emailDTO);
        return "email";
    }

    @PostMapping("/email")
    public String emailUpdate(@Valid @ModelAttribute EmailDTO emailDTO,
                             BindingResult bindingResult, HttpServletRequest request, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("emailDTO", emailDTO);
            return "email";
        }
        String newEmail = emailDTO.getEmail().trim();
        HttpSession session = request.getSession();
        User existingUser = (User) session.getAttribute("user");
        User updatedUser = userService.findUserByEmail(existingUser.getEmail());
        updatedUser.setEmail(newEmail);
        userService.saveUser(updatedUser);
        session.setAttribute("user", updatedUser);
        return "redirect:/api/v1/profile/settings/email?success";
    }

    @GetMapping("/password")
    public String password(Model model){
        model.addAttribute("passwordDTO", new PasswordDTO());
        return "password";
    }

    @PostMapping("/password")
    public String passwordUpdate(@Valid @ModelAttribute PasswordDTO passwordDTO,
                                 BindingResult bindingResult, HttpServletRequest request, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("passwordDTO", passwordDTO);
            return "password";
        }
        String oldPassword = passwordDTO.getOldPassword();
        String newPassword = passwordDTO.getNewPassword();
        String confirmPassword = passwordDTO.getConfirmPassword();
        if (!newPassword.equals(confirmPassword)){
            return "redirect:/api/v1/profile/settings/password?misMatchPass";
        }
        HttpSession session = request.getSession();
        User existingUser = (User) session.getAttribute("user");
        User updatedUser = userService.findUserByEmail(existingUser.getEmail());
        if (!passwordEncoder.matches(oldPassword, updatedUser.getPassword())) {
            return "redirect:/api/v1/profile/settings/password?wrongPassword";
        }
        updatedUser.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(updatedUser);
        session.setAttribute("user", updatedUser);
        return "redirect:/api/v1/profile/settings/password?success";
    }

    public User getUserFromSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        return user;
    }

}
