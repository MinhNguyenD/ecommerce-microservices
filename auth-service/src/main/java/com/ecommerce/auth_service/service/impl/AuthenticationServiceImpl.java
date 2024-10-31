package com.ecommerce.auth_service.service.impl;
import com.ecommerce.auth_service.constant.PredefinedRole;
import com.ecommerce.auth_service.dto.request.LoginRequest;
import com.ecommerce.auth_service.dto.request.RegisterRequest;
import com.ecommerce.auth_service.dto.response.AuthenticationResponse;
import com.ecommerce.auth_service.entity.Role;
import com.ecommerce.auth_service.entity.User;
import com.ecommerce.auth_service.exception.AppException;
import com.ecommerce.auth_service.exception.ErrorCode;
import com.ecommerce.auth_service.mapper.UserMapper;
import com.ecommerce.auth_service.repository.RoleRepository;
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
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

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
    RoleRepository roleRepository;
    @NonFinal
    @Value("${jwt.signKey}")
    String SIGN_KEY;

    public AuthenticationResponse authenticate(LoginRequest loginRequest){
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if(!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);
        String jwtToken = generateJwtToken(user);
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
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findById(PredefinedRole.USER_ROLE).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
        String jwtToken = generateJwtToken(user);
        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(jwtToken)
                .build();
    }

    private String generateJwtToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("ecommerce.com")
                .expirationTime(new Date(Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()))
                .claim("scope", buildAuthScope(user))
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

    /**
     * create a scope (which is jwt identifier of authorization in OAuth2)
     * @param user: authenticated user that have roles attached
     * @return scope String which is in this format "ROLE_ROLE1 PERMISSION1 ROLE_ROLE2 PERMISSION1 PERMISSION2"
     */
    private String buildAuthScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        var roles = user.getRoles();
        if(roles.isEmpty()) return "";
        for(var role : roles){
            stringJoiner.add("ROLE_" + role.getName());
            var permissions = role.getPermissions();
            if(permissions.isEmpty()) continue;
            permissions.forEach(permission -> stringJoiner.add(permission.getName()));
        }
        log.info(stringJoiner.toString());
        return stringJoiner.toString();
    }
}
