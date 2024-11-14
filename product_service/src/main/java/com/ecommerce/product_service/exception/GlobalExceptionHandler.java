package com.ecommerce.product_service.exception;


import com.ecommerce.auth_service.dto.APIResponse;
import com.ecommerce.auth_service.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// allows handling exception across application controllers
@ControllerAdvice
public class GlobalExceptionHandler {


    // method as the class exception handle for Exception class
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<ErrorResponse>> handleGeneralException(Exception ex){
        ErrorResponse error = ErrorResponse.builder()
                .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .info(ex.getMessage())
                .build();
        APIResponse<ErrorResponse> errorResponse = APIResponse.<ErrorResponse>builder()
                .success(false)
                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .data(error).build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // method as the class exception handle for AppException class
    @ExceptionHandler(AppException.class)
    public ResponseEntity<APIResponse<ErrorResponse>> handleAppException(AppException ex){
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse error = ErrorResponse.builder()
                .code(errorCode.getCode())
                .info(ex.getMessage())
                .build();
        APIResponse<ErrorResponse> errorResponse = APIResponse.<ErrorResponse>builder()
                .success(false)
                .message(errorCode.getMessage())
                .data(error).build();
        return ResponseEntity.badRequest().body(errorResponse);
    }


    // MethodArgumentNotValidException is thrown when controller argument validation failed
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<ErrorResponse>> handleArgumentValidationException(MethodArgumentNotValidException ex){
        String enumKey = ex.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try{
            errorCode = ErrorCode.valueOf(enumKey);
        }
        catch (IllegalArgumentException ignored){
            // Error Code will be INVALID_KEY if no enumKey value match with defined ErrorCode enum
        }

        ErrorResponse error = ErrorResponse.builder()
                .code(errorCode.getCode())
                .info(ex.getMessage())
                .build();

        APIResponse<ErrorResponse> errorResponse = APIResponse.<ErrorResponse>builder()
                .success(false)
                .message(errorCode.getMessage())
                .data(error).build();

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
