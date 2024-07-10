package com.academy.Ecommerce.service;

import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.repository.RoleRepository;
import com.academy.Ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public User getUser(Long userId) {
        return userRepository.findById(userId).get();
    }
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
