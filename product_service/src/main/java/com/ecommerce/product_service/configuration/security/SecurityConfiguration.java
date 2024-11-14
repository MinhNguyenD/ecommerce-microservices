package com.ecommerce.product_service.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/**", "/introspect", "/logout", "/refresh","/swagger-ui.html", "/swagger-ui/**", "/api-docs/**"
    };

    @Autowired
    private AppJWTDecoder appJWTDecoder;

    @Value("${jwt.signKey}")
    private String SIGN_KEY;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS)
                .permitAll() // Permit POST requests to PUBLIC_ENDPOINTS
                .requestMatchers(HttpMethod.PUT, PUBLIC_ENDPOINTS)
                .permitAll() // Permit PUT requests to PUBLIC_ENDPOINTS
                .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS)
                .permitAll() // Permit GET requests to PUBLIC_ENDPOINTS
                .requestMatchers(HttpMethod.DELETE, PUBLIC_ENDPOINTS)
                .permitAll() // Permit DELETE requests to PUBLIC_ENDPOINTS
                .anyRequest()
                .authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                .jwtAuthenticationConverter(jwtConverter()) // this to customize the JWT prefix
                .decoder(appJWTDecoder)));

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
