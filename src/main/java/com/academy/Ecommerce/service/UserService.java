package com.academy.Ecommerce.service;

import com.academy.Ecommerce.exception.UserIsNotEnabledException;
import com.academy.Ecommerce.model.Role;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.repository.RoleRepository;
import com.academy.Ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
         return userRepository.findAll();
    }
    public User findUserById(Long id){
        return userRepository.findUserById(id);
    }


    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByConfirmationToken(String token) {
        return userRepository.findByConfirmationToken(token);
    }

    public List<User> findUsersByRole(Role role ){
        return userRepository.findByRoles(role);
    }


    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.deleteById(user.getId());
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }




    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("user not found");

        if (user.isLocked()) {
            throw new LockedException("User account is locked");
        }

        if (!user.isEnabled()) {
            throw new UserIsNotEnabledException("user is not enabled");
        }

        user.setLoginTries(user.getLoginTries() + 1);
        userRepository.save(user);

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role ->
                new SimpleGrantedAuthority(role.getName())).toList();
    }


}
