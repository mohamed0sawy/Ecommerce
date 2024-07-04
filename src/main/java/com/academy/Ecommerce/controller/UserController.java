package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.service.UserService;
import com.academy.Ecommerce.util.AuthenticationUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAll(Authentication authentication) {
        System.out.println("AUTHENTICATED_USER: " + AuthenticationUser.get(authentication));
        return ResponseEntity.ok().body(userService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> getById(@PathVariable Long id, Authentication authentication) {
        System.out.println("AUTHENTICATED_USER: " + AuthenticationUser.get(authentication));
        return ResponseEntity.ok().body(userService.getById(id));
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<User> getById(@RequestParam(required = true) String email, Authentication authentication) {
        System.out.println("AUTHORITIES: " + authentication.getAuthorities());

        System.out.println("AUTHENTICATED_USER: " + AuthenticationUser.get(authentication));
        return ResponseEntity.ok().body(userService.getByEmail(email));
    }
}
