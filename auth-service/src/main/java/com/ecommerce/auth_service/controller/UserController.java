package com.ecommerce.auth_service.controller;
import com.ecommerce.auth_service.dto.APIResponse;
import com.ecommerce.auth_service.dto.request.UserCreateRequest;
import com.ecommerce.auth_service.dto.request.UserUpdateRequest;
import com.ecommerce.auth_service.dto.response.UserResponse;
import com.ecommerce.auth_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userServiceImpl;

    @PostMapping
    public ResponseEntity<APIResponse<UserResponse>> createUser(@RequestBody @Valid UserCreateRequest userCreateRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.<UserResponse>builder()
                .success(true)
                .data(userServiceImpl.createUser(userCreateRequest))
                .build());
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<UserResponse>>> getAllUsers(){
        return ResponseEntity.ok(APIResponse.<List<UserResponse>>builder()
                .success(true)
                .data(userServiceImpl.getUsers())
                .build());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<APIResponse<UserResponse>> getUser(@PathVariable String userId){
        return ResponseEntity.ok(APIResponse.<UserResponse>builder()
                .success(true)
                .data(userServiceImpl.getUser(userId))
                .build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<APIResponse<UserResponse>> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest userUpdateRequest){
        return ResponseEntity.ok(APIResponse.<UserResponse>builder()
                .success(true)
                .data(userServiceImpl.updateUser(userId, userUpdateRequest))
                .build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable String userId){
        userServiceImpl.deleteUser(userId);
        return ResponseEntity.ok(APIResponse.<UserResponse>builder()
                .success(true)
                .message("Deleted user with id " + userId)
                .build());
    }
 }
