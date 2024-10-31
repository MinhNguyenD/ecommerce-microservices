package com.ecommerce.auth_service.controller;

import com.ecommerce.auth_service.dto.APIResponse;
import com.ecommerce.auth_service.dto.request.LoginRequest;
import com.ecommerce.auth_service.dto.request.LogoutRequest;
import com.ecommerce.auth_service.dto.request.RefreshRequest;
import com.ecommerce.auth_service.dto.request.RegisterRequest;
import com.ecommerce.auth_service.dto.response.AuthenticationResponse;
import com.ecommerce.auth_service.dto.response.RefreshTokenResponse;
import com.ecommerce.auth_service.service.AuthenticationService;
import com.ecommerce.auth_service.service.UserService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping(value="api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthController {
    UserService userService;
    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<AuthenticationResponse>> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(APIResponse.success(authenticationService.register(registerRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthenticationResponse>> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(APIResponse.success(authenticationService.authenticate(loginRequest)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<APIResponse<RefreshTokenResponse>> refresh(@RequestBody RefreshRequest refreshRequest) throws ParseException, JOSEException {
        return ResponseEntity.ok(APIResponse.success(authenticationService.refreshAccessToken(refreshRequest)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authenticationService.logout(logoutRequest);
        return ResponseEntity.ok().build();
    }
}
