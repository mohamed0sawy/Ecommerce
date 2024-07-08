package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.model.Role;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.repository.RoleRepository;
import com.academy.Ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


@GetMapping
    public String listAdmins(Model model) {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        List<User> admins = userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(adminRole))
                .toList();
        model.addAttribute("admins", admins);
        return "admin/index";
    }

    @GetMapping("/add")
    public String addAdminForm(Model model) {
        model.addAttribute("admin", new User());
        return "admin/create";
    }

    @PostMapping("/add")
    public String addAdmin(@ModelAttribute User admin) {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        List<Role> roles = new ArrayList<>();
        roles.add(adminRole);
        admin.setRoles(roles);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        userRepository.save(admin);
        return "redirect:/admins";
    }

    @GetMapping("/edit/{id}")
    public String editAdminForm(@PathVariable Long id, Model model) {
        User admin = userRepository.findById(id).orElse(null);
        model.addAttribute("admin", admin);
        return "admin/edit";
    }

    @PostMapping("/edit")
    public String editAdmin(@ModelAttribute User admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        userRepository.save(admin);
        return "redirect:/admins";
    }

    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admins";
    }
}
