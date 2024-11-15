package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.request.ProductRequest;
import com.ecommerce.product_service.dto.response.PageResponse;
import com.ecommerce.product_service.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse getProduct(String id);
    PageResponse<ProductResponse> getProducts(int pageNum, int pageSize);
    ProductResponse createProduct(ProductRequest productRequest);
    ProductResponse updateProduct(String id, ProductRequest productRequest);
    String deleteProduct(String id);
}
