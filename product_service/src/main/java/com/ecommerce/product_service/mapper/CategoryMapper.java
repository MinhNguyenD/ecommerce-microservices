package com.ecommerce.product_service.mapper;


import com.ecommerce.product_service.dto.request.CategoryRequest;
import com.ecommerce.product_service.dto.response.CategoryResponse;
import com.ecommerce.product_service.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);
    Category toCategory(CategoryRequest categoryRequest);
    void updateCategory(@MappingTarget Category category, CategoryRequest categoryRequest);
}
