package com.ecommerce.product_service.controller;


import com.ecommerce.product_service.dto.APIResponse;
import com.ecommerce.product_service.dto.response.InventoryResponse;
import com.ecommerce.product_service.dto.response.ProductResponse;
import com.ecommerce.product_service.service.InventoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = "/api/v1/inventories")
public class InventoryController {
    InventoryService inventoryService;
    @GetMapping("/{productId}")
    public ResponseEntity<APIResponse<InventoryResponse>> getProduct(@PathVariable String productId) {
        return ResponseEntity.ok(APIResponse.success(inventoryService.getInventoryByProductId(productId)));
    }
}
