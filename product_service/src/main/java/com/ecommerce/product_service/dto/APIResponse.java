package com.ecommerce.product_service.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {
    boolean success;
    String message;
    T data;

    public static <T> APIResponse<T> success(T data){
        return APIResponse.<T>builder()
                .success(true)
                .message("Successful")
                .data(data)
                .build();
    }

    public static <T> APIResponse<T> error(T data){
        return APIResponse.<T>builder()
                .success(false)
                .message("Error")
                .data(data)
                .build();
    }
}
