package com.academy.Ecommerce.service.impl;

import com.academy.Ecommerce.exception.ApiError;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.repository.UserRepository;
import com.academy.Ecommerce.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> ApiError.badRequest("User not found with id=" + id));
    }

    @Override
    public User getByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw ApiError.badRequest("User not found with email=" + email);
        }

        return user;
    }

    @Override
    public User getByEmailOrNull(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByEmail(username);
        return user;
    }
}