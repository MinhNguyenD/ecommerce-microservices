package com.ecommerce.auth_service.service.impl;

import com.ecommerce.auth_service.constant.PredefinedRole;
import com.ecommerce.auth_service.dto.request.*;
import com.ecommerce.auth_service.dto.response.AuthenticationResponse;
import com.ecommerce.auth_service.dto.response.IntrospectResponse;
import com.ecommerce.auth_service.dto.response.RefreshTokenResponse;
import com.ecommerce.auth_service.entity.InvalidatedToken;
import com.ecommerce.auth_service.entity.Role;
import com.ecommerce.auth_service.entity.User;
import com.ecommerce.auth_service.exception.AppException;
import com.ecommerce.auth_service.exception.ErrorCode;
import com.ecommerce.auth_service.mapper.UserMapper;
import com.ecommerce.auth_service.repository.InvalidatedTokenRepository;
import com.ecommerce.auth_service.repository.RoleRepository;
import com.ecommerce.auth_service.repository.UserRepository;
import com.ecommerce.auth_service.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
// create constructor that has all "final" and non-null fields
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);;
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    @NonFinal
    @Value("${jwt.signKey}")
    String SIGN_KEY;

    public AuthenticationResponse authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);
        String jwtAccessToken = generateJwtAccessToken(user);
        String jwtRefreshToken = generateJwtRefreshToken(user);
        return AuthenticationResponse.builder()
                .authenticated(true)
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        boolean userExists = userRepository.findByUsername(registerRequest.getUsername()).isPresent();
        if (userExists) throw new AppException(ErrorCode.USER_EXISTED);
        User user = userMapper.toUser(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findById(PredefinedRole.USER_ROLE).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
        String jwtAccessToken = generateJwtAccessToken(user);
        String jwtRefreshToken = generateJwtRefreshToken(user);
        return AuthenticationResponse.builder()
                .authenticated(true)
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    public RefreshTokenResponse refreshAccessToken(RefreshRequest refreshRequest) throws ParseException, JOSEException {
        var verifiedRefreshJWT = verifyToken(refreshRequest.getRefreshToken());
        SignedJWT verifiedAccessTokenJWT;
        try{
            verifiedAccessTokenJWT = verifyToken(refreshRequest.getAccessToken());
            invalidateToken(verifiedAccessTokenJWT);
        }
        catch(Exception e){
            // do not need to invalidate if the currentAccessToken is not verified
        }

        // generate new access token
        // User not found but it relates to jwt token authentication so unauthenticated error
        User user = userRepository.findByUsername(verifiedRefreshJWT.getJWTClaimsSet().getSubject()).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        String newAccessToken = generateJwtAccessToken(user);
        return RefreshTokenResponse.builder()
                .authenticated(true)
                .accessToken(newAccessToken)
                .build();
    }

    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        var verifiedToken = verifyToken(logoutRequest.getToken());
        invalidateToken(verifiedToken);
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public IntrospectResponse introspect(IntrospectRequest request){
        try{
            verifyToken(request.getToken());
        } catch (Exception e) {
            return IntrospectResponse.builder()
                    .valid(false)
                    .build();
        }
        return IntrospectResponse.builder()
                .valid(true)
                .build();
    }

    private void invalidateToken(SignedJWT signedJWT) throws ParseException {
        String jwtID = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        invalidatedTokenRepository.save(InvalidatedToken
                .builder()
                .expiryDate(expiryDate)
                .id(jwtID)
                .build());
    }

    private String generateJwtAccessToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("ecommerce.com")
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(new Date(Instant.now().plus(15, ChronoUnit.MINUTES).toEpochMilli()))
                .claim("scope", buildAuthScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error creating jwt token");
            throw new RuntimeException(e);
        }
    }

    private String generateJwtRefreshToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("ecommerce.com")
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(new Date(Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error creating jwt refresh token");
            throw new RuntimeException(e);
        }
    }


    /**
     * create a scope (which is jwt identifier of authorization in OAuth2)
     *
     * @param user: authenticated user that have roles attached
     * @return scope String which is in this format "ROLE_ROLE1 PERMISSION1 ROLE_ROLE2 PERMISSION1 PERMISSION2"
     */
    private String buildAuthScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        var roles = user.getRoles();
        if (roles.isEmpty()) return "";
        for (var role : roles) {
            stringJoiner.add("ROLE_" + role.getName());
            var permissions = role.getPermissions();
            if (permissions.isEmpty()) continue;
            permissions.forEach(permission -> stringJoiner.add(permission.getName()));
        }
        log.info(stringJoiner.toString());
        return stringJoiner.toString();
    }

    private SignedJWT verifyToken(String jwtToken) throws ParseException, JOSEException {
        var verifier = new MACVerifier(SIGN_KEY.getBytes());
        var signedJWT = SignedJWT.parse(jwtToken);
        Date expiryTime = signedJWT
                .getJWTClaimsSet()
                .getExpirationTime();
        if(!(signedJWT.verify(verifier) && expiryTime.after(new Date()))){
            return signedJWT;
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }
}
