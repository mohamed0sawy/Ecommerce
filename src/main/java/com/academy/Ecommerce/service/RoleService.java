package com.academy.Ecommerce.service;

import com.academy.Ecommerce.model.Role;
import com.academy.Ecommerce.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role findRoleByName(String role){
        return roleRepository.findByName(role);
    }
}
