package com.academy.Ecommerce.service;

import com.academy.Ecommerce.model.Role;

import java.util.List;

public interface RoleService {
    public List<Role> getAll();
    public Role getById(Long id);
    public Role getByName(String name);
    public Role getByNameOrNull(String name);
    public Role save(Role role);
}
