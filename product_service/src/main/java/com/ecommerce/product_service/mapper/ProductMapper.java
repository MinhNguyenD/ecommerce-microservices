package com.ecommerce.product_service.mapper;

import com.ecommerce.product_service.dto.request.ProductRequest;
import com.ecommerce.product_service.dto.response.ProductResponse;
import com.ecommerce.product_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductRequest productRequest);
    ProductResponse toProductResponse(Product product);
    void updateProduct(@MappingTarget Product product, ProductRequest productRequest);
}
