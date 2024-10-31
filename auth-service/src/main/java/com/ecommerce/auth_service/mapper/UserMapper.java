package com.ecommerce.auth_service.mapper;

import com.ecommerce.auth_service.dto.request.RegisterRequest;
import com.ecommerce.auth_service.dto.request.UserCreateRequest;
import com.ecommerce.auth_service.dto.request.UserUpdateRequest;
import com.ecommerce.auth_service.dto.response.UserResponse;
import com.ecommerce.auth_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest userCreateRequest);

    User toUser(RegisterRequest registerRequest);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
