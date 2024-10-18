package com.ecommerce.auth_service.repository;

import com.ecommerce.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
