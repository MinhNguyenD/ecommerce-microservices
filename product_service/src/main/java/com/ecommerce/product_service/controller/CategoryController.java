package com.ecommerce.product_service.controller;


import com.ecommerce.product_service.dto.APIResponse;
import com.ecommerce.product_service.dto.request.CategoryRequest;
import com.ecommerce.product_service.dto.response.CategoryResponse;
import com.ecommerce.product_service.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = "/api/v1/categories")
public class CategoryController {
    CategoryService categoryService;

    @GetMapping
    public ResponseEntity<APIResponse<List<CategoryResponse>>> getCategories(){
        return ResponseEntity.ok(APIResponse.success(categoryService.getCategories()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<CategoryResponse>> getCategory(@PathVariable String id){
        return ResponseEntity.ok(APIResponse.success(categoryService.getCategory(id)));
    }
    @PostMapping
    public ResponseEntity<APIResponse<CategoryResponse>> createCategory(@RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success(categoryService.createCategory(categoryRequest)));
    }

    @PutMapping
    public ResponseEntity<APIResponse<CategoryResponse>> updateCategory(@RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.ok(APIResponse.success(categoryService.updateCategory(categoryRequest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deleteCategory(String id){
        return ResponseEntity.ok(APIResponse.success(categoryService.deleteCategory(id)));
    }
}
