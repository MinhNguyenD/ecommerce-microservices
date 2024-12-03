package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InventoryRepository extends JpaRepository<Inventory, String> {
    @Query("SELECT i from Inventory i where i.product.id = :productId")
    Inventory findByProductId(@Param("productId") String productId);
}
