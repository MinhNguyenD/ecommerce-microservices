package com.ecommerce.auth_service.service.impl;

import com.ecommerce.auth_service.dto.request.PermissionRequest;
import com.ecommerce.auth_service.dto.response.PermissionResponse;
import com.ecommerce.auth_service.entity.Permission;
import com.ecommerce.auth_service.exception.AppException;
import com.ecommerce.auth_service.exception.ErrorCode;
import com.ecommerce.auth_service.mapper.PermissionMapper;
import com.ecommerce.auth_service.repository.PermissionRepository;
import com.ecommerce.auth_service.service.PermissionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse getPermission(String name) {
        return permissionMapper.toPermissionResponse(permissionRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED)));
    }

    @Override
    public List<PermissionResponse> getPermissions() {
        return permissionRepository.findAll().stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Override
    public PermissionResponse createPermission(PermissionRequest createRequest) {
        boolean permissionExists = permissionRepository.findById(createRequest.getName()).isPresent();
        if (permissionExists) throw new AppException(ErrorCode.PERMISSION_EXISTED);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permissionMapper.toPermission(createRequest)));
    }

    @Override
    public PermissionResponse updatePermission(PermissionRequest updateRequest) {
        Permission permission = permissionRepository.findById(updateRequest.getName()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        permissionMapper.updatePermission(permission, updateRequest);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public String deletePermission(String name) {
        Permission permission = permissionRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        permissionRepository.delete(permission);
        return "Deleted Permission with name " + name;
    }
}
