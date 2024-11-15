package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.request.CategoryRequest;
import com.ecommerce.product_service.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest categoryRequest);
    CategoryResponse updateCategory(CategoryRequest categoryRequest);
    List<CategoryResponse> getCategories();
    CategoryResponse getCategory(String id);
    String deleteCategory(String id);
}
