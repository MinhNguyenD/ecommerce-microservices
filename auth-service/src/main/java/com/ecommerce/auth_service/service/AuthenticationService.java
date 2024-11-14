package com.ecommerce.auth_service.service;

import com.ecommerce.auth_service.dto.request.*;
import com.ecommerce.auth_service.dto.response.AuthenticationResponse;
import com.ecommerce.auth_service.dto.response.IntrospectResponse;
import com.ecommerce.auth_service.dto.response.RefreshTokenResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(LoginRequest loginRequest);
    AuthenticationResponse register(RegisterRequest registerRequest);
    void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException;
    IntrospectResponse introspect(IntrospectRequest token);
    RefreshTokenResponse refreshAccessToken(RefreshRequest refreshRequest) throws ParseException, JOSEException;
}
