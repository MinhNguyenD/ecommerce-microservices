package com.ecommerce.auth_service.service;

import com.ecommerce.auth_service.dto.request.LoginRequest;
import com.ecommerce.auth_service.dto.request.LogoutRequest;
import com.ecommerce.auth_service.dto.request.RefreshRequest;
import com.ecommerce.auth_service.dto.request.RegisterRequest;
import com.ecommerce.auth_service.dto.response.AuthenticationResponse;
import com.ecommerce.auth_service.dto.response.RefreshTokenResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(LoginRequest loginRequest);
    AuthenticationResponse register(RegisterRequest registerRequest);
    void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException;
    boolean introspect(String token);
    RefreshTokenResponse refreshAccessToken(RefreshRequest refreshRequest) throws ParseException, JOSEException;
}
