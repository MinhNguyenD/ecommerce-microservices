package com.ecommerce.auth_service.service;

import com.ecommerce.auth_service.dto.request.UserCreateRequest;
import com.ecommerce.auth_service.dto.request.UserUpdateRequest;
import com.ecommerce.auth_service.dto.response.PageResponse;
import com.ecommerce.auth_service.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest userCreateRequest);
    PageResponse<UserResponse> getUsers(int pageNum, int pageSize);
    UserResponse getUser(String userId);
    UserResponse updateUser(String userID, UserUpdateRequest userUpdateRequest);
    String deleteUser(String userId);
}
