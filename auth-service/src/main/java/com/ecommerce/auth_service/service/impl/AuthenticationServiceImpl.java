package com.ecommerce.auth_service.service.impl;
import com.ecommerce.auth_service.dto.request.LoginRequest;
import com.ecommerce.auth_service.dto.request.RegisterRequest;
import com.ecommerce.auth_service.dto.response.AuthenticationResponse;
import com.ecommerce.auth_service.entity.User;
import com.ecommerce.auth_service.exception.AppException;
import com.ecommerce.auth_service.exception.ErrorCode;
import com.ecommerce.auth_service.mapper.UserMapper;
import com.ecommerce.auth_service.repository.UserRepository;
import com.ecommerce.auth_service.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
// create constructor that has all "final" and non-null fields
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    UserMapper userMapper;
    @NonFinal
    @Value("${jwt.signKey}")
    String SIGN_KEY;

    public AuthenticationResponse authenticate(LoginRequest loginRequest){
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if(!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);
        String jwtToken = generateJwtToken(user.getUsername());
        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse register(RegisterRequest registerRequest){
        boolean userExists = userRepository.findByUsername(registerRequest.getUsername()).isPresent();
        if(userExists) throw new AppException(ErrorCode.USER_EXISTED);
        User user = userMapper.toUser(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        String jwtToken = generateJwtToken(user.getUsername());
        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(jwtToken)
                .build();
    }

    private String generateJwtToken(String username){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("ecommerce.com")
                .expirationTime(new Date(Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()))
//                .claim("customClaim", "custom value")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try{
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
            return jwsObject.serialize();
        }
        catch (JOSEException e){
            log.error("Error creating jwt token");
            throw new RuntimeException(e);
        }
    }
}
