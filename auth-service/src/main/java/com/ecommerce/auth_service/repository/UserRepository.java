package com.ecommerce.auth_service.repository;

import com.ecommerce.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    // Optional because the user might not exist
    Optional<User> findByUsername(String username);
}
