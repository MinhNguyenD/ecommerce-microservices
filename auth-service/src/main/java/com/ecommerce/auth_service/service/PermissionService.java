package com.ecommerce.auth_service.service;

import com.ecommerce.auth_service.dto.request.PermissionRequest;
import com.ecommerce.auth_service.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse getPermission(String name);

    List<PermissionResponse> getPermissions();

    PermissionResponse createPermission(PermissionRequest createRequest);

    PermissionResponse updatePermission(PermissionRequest updateRequest);

    String deletePermission(String name);
}
