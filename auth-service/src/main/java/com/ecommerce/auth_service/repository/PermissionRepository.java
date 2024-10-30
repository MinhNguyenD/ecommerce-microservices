package com.ecommerce.auth_service.repository;

import com.ecommerce.auth_service.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}
