package com.academy.Ecommerce.repository;

import com.academy.Ecommerce.model.Role;
import com.academy.Ecommerce.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.logging.Logger;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    User findByConfirmationToken(String confirmationToken);
    List<User> findByRoles(Role role);

}
