package com.ecommerce.auth_service.service;

import com.ecommerce.auth_service.dto.request.RoleRequest;
import com.ecommerce.auth_service.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse getRole(String name);

    List<RoleResponse> getRoles();

    RoleResponse createRole(RoleRequest createRequest);

    RoleResponse updateRole(RoleRequest updateRequest);

    String deleteRole(String name);
}
