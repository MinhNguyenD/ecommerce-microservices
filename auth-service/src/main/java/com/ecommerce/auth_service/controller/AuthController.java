package com.ecommerce.auth_service.controller;

import com.ecommerce.auth_service.dto.APIResponse;
import com.ecommerce.auth_service.dto.request.LoginRequest;
import com.ecommerce.auth_service.dto.request.RegisterRequest;
import com.ecommerce.auth_service.dto.response.AuthenticationResponse;
import com.ecommerce.auth_service.service.AuthenticationService;
import com.ecommerce.auth_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    UserService userService;
    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<AuthenticationResponse>> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(APIResponse.<AuthenticationResponse>builder()
                .success(true)
                .message("Register successful")
                .data(authenticationService.register(registerRequest))
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthenticationResponse>> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(APIResponse.<AuthenticationResponse>builder()
                .success(true)
                .message("Login successful")
                .data(authenticationService.authenticate(loginRequest))
                .build());
    }
}
