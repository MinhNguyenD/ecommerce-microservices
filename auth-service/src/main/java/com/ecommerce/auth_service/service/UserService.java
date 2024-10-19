package com.ecommerce.auth_service.service;

import com.ecommerce.auth_service.dto.request.UserCreateRequest;
import com.ecommerce.auth_service.dto.request.UserUpdateRequest;
import com.ecommerce.auth_service.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest userCreateRequest);
    List<UserResponse> getUsers();
    UserResponse getUser(String userId);
    UserResponse updateUser(String userID, UserUpdateRequest userUpdateRequest);
    void deleteUser(String userId);
}
