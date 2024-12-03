package com.ecommerce.product_service.mapper;

import com.ecommerce.product_service.dto.response.InventoryResponse;
import com.ecommerce.product_service.entity.Inventory;
import com.ecommerce.product_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    @Mapping(target = "productId", source = "product")
    InventoryResponse toInventoryResponse(Inventory inventory);

    default String mapProductToProductId(Product product) {
        return product.getId();
    }
}
