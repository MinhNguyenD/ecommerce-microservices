package com.ecommerce.product_service.controller;


import com.ecommerce.product_service.dto.APIResponse;
import com.ecommerce.product_service.dto.request.ProductRequest;
import com.ecommerce.product_service.dto.response.PageResponse;
import com.ecommerce.product_service.dto.response.ProductResponse;
import com.ecommerce.product_service.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="api/v1/products")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<ProductResponse>> getProduct(@PathVariable String id) {
        return ResponseEntity.ok(APIResponse.success(productService.getProduct(id)));
    }

    @GetMapping
    public ResponseEntity<APIResponse<PageResponse<ProductResponse>>> getProducts(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(APIResponse.success(productService.getProducts(pageNum, pageSize)));
    }


    @PostMapping
    public ResponseEntity<APIResponse<ProductResponse>> createProduct(@RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(APIResponse.success(productService.createProduct(productRequest)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<ProductResponse>> updateProduct(@PathVariable String id, @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(APIResponse.success(productService.updateProduct(id, productRequest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deleteProduct(@PathVariable String id) {
        return ResponseEntity.ok(APIResponse.success(productService.deleteProduct(id)));
    }
}
