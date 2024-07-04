package com.academy.Ecommerce.service;


import com.academy.Ecommerce.model.User;

import java.util.List;

public interface UserService {
    public List<User> getAll();
    public User getById(Long id);
    public User getByEmail(String email);
    public User getByEmailOrNull(String email);
    public User save(User user);
}
