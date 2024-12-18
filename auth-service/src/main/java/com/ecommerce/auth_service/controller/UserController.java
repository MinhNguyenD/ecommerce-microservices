package com.ecommerce.auth_service.controller;
import com.ecommerce.auth_service.dto.APIResponse;
import com.ecommerce.auth_service.dto.request.UserCreateRequest;
import com.ecommerce.auth_service.dto.request.UserUpdateRequest;
import com.ecommerce.auth_service.dto.response.PageResponse;
import com.ecommerce.auth_service.dto.response.UserResponse;
import com.ecommerce.auth_service.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User", description = "User Endpoints")
public class UserController {
    UserService userServiceImpl;

    @PostMapping
    public ResponseEntity<APIResponse> createUser(@RequestBody @Valid UserCreateRequest userCreateRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success(userServiceImpl.createUser(userCreateRequest)));
    }

    @GetMapping
    public ResponseEntity<APIResponse> getAllUsers(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum , @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize){
        return ResponseEntity.ok(APIResponse.success(userServiceImpl.getUsers(pageNum, pageSize)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<APIResponse> getUser(@PathVariable String userId){
        return ResponseEntity.ok(APIResponse.success(userServiceImpl.getUser(userId)));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<APIResponse> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest userUpdateRequest){
        return ResponseEntity.ok(APIResponse.success(userServiceImpl.updateUser(userId, userUpdateRequest)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable String userId){
        return ResponseEntity.ok(APIResponse.success(userServiceImpl.deleteUser(userId)));
    }
 }
