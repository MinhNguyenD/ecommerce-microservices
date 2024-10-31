package com.ecommerce.auth_service.service.impl;

import com.ecommerce.auth_service.dto.request.RoleRequest;
import com.ecommerce.auth_service.dto.response.RoleResponse;
import com.ecommerce.auth_service.entity.Permission;
import com.ecommerce.auth_service.entity.Role;
import com.ecommerce.auth_service.exception.AppException;
import com.ecommerce.auth_service.exception.ErrorCode;
import com.ecommerce.auth_service.mapper.RoleMapper;
import com.ecommerce.auth_service.repository.PermissionRepository;
import com.ecommerce.auth_service.repository.RoleRepository;
import com.ecommerce.auth_service.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    @Override
    public RoleResponse getRole(String name) {
        return roleMapper.toRoleResponse(roleRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED)));
    }

    @Override
    public List<RoleResponse> getRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    @Override
    public RoleResponse createRole(RoleRequest createRequest) {
        boolean roleExists = roleRepository.findById(createRequest.getName()).isPresent();
        if (roleExists) throw new AppException(ErrorCode.ROLE_EXISTED);
        Role role = roleMapper.toRole(createRequest);
        List<Permission> permissions = permissionRepository.findAllById(createRequest.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public RoleResponse updateRole(RoleRequest updateRequest) {
        Role role = roleRepository.findById(updateRequest.getName()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roleMapper.updateRole(role, updateRequest);
        List<Permission> permissions = permissionRepository.findAllById(updateRequest.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public String deleteRole(String name) {
        Role role = roleRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roleRepository.delete(role);
        return "Deleted Role with name " + name;
    }
}
