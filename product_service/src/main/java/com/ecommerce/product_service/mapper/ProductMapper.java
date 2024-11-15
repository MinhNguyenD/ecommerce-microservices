package com.ecommerce.product_service.mapper;

import com.ecommerce.product_service.dto.request.ProductRequest;
import com.ecommerce.product_service.dto.response.ProductResponse;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.exception.AppException;
import com.ecommerce.product_service.exception.ErrorCode;
import com.ecommerce.product_service.repository.CategoryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categories", ignore = true)
    Product toProduct(ProductRequest productRequest);

    @Mapping(target = "categoryIds", source = "categories")
    ProductResponse toProductResponse(Product product);

    @Mapping(target = "categories", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductRequest productRequest);

    default List<String> mapCategoriesToIds(Set<Category> categories) {
        return categories.stream().map(Category::getName).collect(Collectors.toList());
    }
}
