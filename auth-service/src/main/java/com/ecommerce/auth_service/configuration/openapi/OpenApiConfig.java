package com.ecommerce.auth_service.configuration.openapi;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        servers = {
                @Server(
                        url = "http://localhost:8080/auth",
                        description = "DEV environment"
                )
        },
        info = @Info(
                title = "Identity Service",
                description = "Identity Service in Ecommerce Microservices"
        ),
        security = {
                @SecurityRequirement(name = "JWTauth")
        }
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "JWTauth",
        description = "JWT Authentication Security Scheme",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer"
)
public class OpenApiConfig {
}
