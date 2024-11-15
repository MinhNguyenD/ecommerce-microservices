package com.ecommerce.auth_service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    ROLE_EXISTED(1003, "Role existed", HttpStatus.BAD_REQUEST),
    PERMISSION_EXISTED(1004, "Permission existed", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(1005, "Permission not existed", HttpStatus.NOT_FOUND),


    USERNAME_INVALID(1006, "Username must be at least 6 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1007, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1008, "User not existed", HttpStatus.NOT_FOUND),
    ROLE_NOT_EXISTED(1009, "Role not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1010, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1011, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1012, "Your age must be at least 18", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
