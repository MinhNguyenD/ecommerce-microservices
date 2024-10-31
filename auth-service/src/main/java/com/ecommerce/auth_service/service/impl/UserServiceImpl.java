package com.ecommerce.auth_service.service.impl;


import com.ecommerce.auth_service.constant.PredefinedRole;
import com.ecommerce.auth_service.dto.request.UserCreateRequest;
import com.ecommerce.auth_service.dto.request.UserUpdateRequest;
import com.ecommerce.auth_service.dto.response.PageResponse;
import com.ecommerce.auth_service.dto.response.UserResponse;
import com.ecommerce.auth_service.entity.Role;
import com.ecommerce.auth_service.entity.User;
import com.ecommerce.auth_service.exception.AppException;
import com.ecommerce.auth_service.exception.ErrorCode;
import com.ecommerce.auth_service.mapper.UserMapper;
import com.ecommerce.auth_service.repository.RoleRepository;
import com.ecommerce.auth_service.repository.UserRepository;
import com.ecommerce.auth_service.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
// create constructor that has all "final" and non-null fields
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    RoleRepository roleRepository;

    @Override
    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        User user = userMapper.toUser(userCreateRequest);
        user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findById(PredefinedRole.USER_ROLE).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roles.add(userRole);
        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public PageResponse<UserResponse> getUsers(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<User> usersPage = userRepository.findAll(pageable);
        List<UserResponse> usersList = usersPage.getContent().stream().map(userMapper::toUserResponse).toList();
        return PageResponse.<UserResponse>builder()
                .currentPage(pageNum)
                .pageSize(usersPage.getSize())
                .totalPages(usersPage.getTotalPages())
                .totalElements(usersPage.getTotalElements())
                .data(usersList)
                .build();
    }

    @Override
    public UserResponse getUser(String userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUser(String userID, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userID).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, userUpdateRequest);

        var roles = roleRepository.findAllById(userUpdateRequest.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public String deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userRepository.delete(user);
        return "Deleted user with id " + userId;
    }
}
