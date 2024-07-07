package com.academy.Ecommerce.service;

import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.repository.RoleRepository;
import com.academy.Ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RoleRepository roleRepository;
//    private final UserRepository userRepository;

    @Autowired
    private UserRepository userRepository;

    public User findUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElse(null); // Handle the case where the user is not found
    }

}
