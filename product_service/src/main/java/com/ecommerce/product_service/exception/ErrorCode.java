package com.ecommerce.product_service.exception;

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
    PERMISSION_NOT_EXISTED(1016, "Permission not existed", HttpStatus.NOT_FOUND),
    USER_NOT_EXISTED(1008, "User not existed", HttpStatus.NOT_FOUND),
    ROLE_NOT_EXISTED(1009, "Role not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1010, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1011, "You do not have permission", HttpStatus.FORBIDDEN),
    PRODUCT_EXISTED(1013, "Product existed", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(1014, "Category existed", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED(1015, "Product not existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1015, "Category not existed", HttpStatus.BAD_REQUEST);



    int code;
    String message;
    HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
