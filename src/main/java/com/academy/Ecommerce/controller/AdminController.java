package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.model.Role;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.repository.RoleRepository;
import com.academy.Ecommerce.repository.UserRepository;
import com.academy.Ecommerce.service.EmailService;
import com.academy.Ecommerce.service.RoleService;
import com.academy.Ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admins")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
public class AdminController {

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);



    @GetMapping
    public String listAdmins(Model model) {
        Role adminRole = roleService.findRoleByName("ROLE_ADMIN");
        Role superAdminRole = roleService.findRoleByName("ROLE_SUPER_ADMIN");

        List<User> admins = userService.findUsersByRole(adminRole);
        List<User> superAdmins= userService.findUsersByRole(superAdminRole);
        List<User> allAdmins = Stream.concat(admins.stream(), superAdmins.stream())
                .collect(Collectors.toList());



        model.addAttribute("admins", allAdmins);
        model.addAttribute("superAdminRole",superAdminRole);

        return "admin/index";
    }

    @GetMapping("/create")
    public String showCreateAdminForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/create";
    }

    @PostMapping("/create")
    public String createAdmin(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        // Generate random password
        String randomPassword = UUID.randomUUID().toString().substring(0, 8);
        Role adminRole = roleService.findRoleByName("ROLE_ADMIN");
        user.setPassword(passwordEncoder.encode(randomPassword));
        user.setEnabled(true);

        user.setRoles(Arrays.asList(adminRole));

        userService.saveUser(user);

        emailService.sendEmail(user.getEmail(), "Your Admin Account", "Your password is: " + randomPassword);

        redirectAttributes.addFlashAttribute("message", "Admin created successfully and email sent!");
        return "redirect:/admins";
    }





    @GetMapping("/edit/{id}")
    public String editAdminForm(@PathVariable Long id, Model model) {
        User admin = userService.findUserById(id);
        model.addAttribute("admin", admin);
        return "admin/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateAdmin(@PathVariable("id") Long id, @ModelAttribute("admin") User admin, BindingResult result, Model model) {
        if (result.hasErrors()) {
            admin.setId(id);
            return "admin/edit";
        }

        User existingAdmin = userService.findUserById(id);
        if (existingAdmin == null) {
            result.rejectValue("id", "error.admin", "Admin not found");
            return "admin/edit";
        }
        admin.setEnabled(existingAdmin.isEnabled());

        admin.setRoles(existingAdmin.getRoles());

        userService.saveUser(admin);
        return "redirect:/admins";
    }


    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable Long id) {
       User admin= userService.findUserById(id);
        userService.deleteUser(admin);
        return "redirect:/admins";
    }
    @PutMapping(value = "/toggle-status/{adminId}")
    public ResponseEntity<?> toggleAdminStatus(@PathVariable Long adminId) {

        // Fetch the admin from the database
        User admin = userService.findUserById(adminId);
        if (admin != null) {
            admin.setEnabled(!admin.isEnabled());
            userService.saveUser(admin);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(admin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
