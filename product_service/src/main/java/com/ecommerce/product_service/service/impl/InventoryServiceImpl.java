package com.ecommerce.product_service.service.impl;

import com.ecommerce.product_service.dto.response.InventoryResponse;
import com.ecommerce.product_service.mapper.InventoryMapper;
import com.ecommerce.product_service.repository.InventoryRepository;
import com.ecommerce.product_service.service.InventoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryServiceImpl implements InventoryService {
    InventoryRepository inventoryRepository;
    InventoryMapper inventoryMapper;
    @Override
    public InventoryResponse getInventoryByProductId(String productId) {
        return inventoryMapper.toInventoryResponse(inventoryRepository.findByProductId(productId));
    }

    @Override
    public InventoryResponse updateInventoryProductId(String productId) {
        return null;
    }
}
