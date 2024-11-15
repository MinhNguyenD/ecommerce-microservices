package com.ecommerce.product_service.service.impl;

import com.ecommerce.product_service.dto.request.CategoryRequest;
import com.ecommerce.product_service.dto.response.CategoryResponse;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.exception.AppException;
import com.ecommerce.product_service.exception.ErrorCode;
import com.ecommerce.product_service.mapper.CategoryMapper;
import com.ecommerce.product_service.repository.CategoryRepository;
import com.ecommerce.product_service.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category = categoryRepository.save(categoryMapper.toCategory(categoryRequest));
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(categoryRequest.getName()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        categoryMapper.updateCategory(category, categoryRequest);
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toCategoryResponse).toList();
    }

    @Override
    public CategoryResponse getCategory(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public String deleteCategory(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        categoryRepository.delete(category);
        return "Deleted category " + category.getName();
    }
}
