package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.response.InventoryResponse;

public interface InventoryService {
    InventoryResponse getInventoryByProductId(String productId);
    InventoryResponse updateInventoryProductId(String productId);
}
