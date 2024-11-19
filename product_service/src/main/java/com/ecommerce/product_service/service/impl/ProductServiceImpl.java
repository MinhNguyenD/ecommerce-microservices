package com.ecommerce.product_service.service.impl;

import com.ecommerce.product_service.dto.request.ProductRequest;
import com.ecommerce.product_service.dto.response.PageResponse;
import com.ecommerce.product_service.dto.response.ProductResponse;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.exception.AppException;
import com.ecommerce.product_service.exception.ErrorCode;
import com.ecommerce.product_service.mapper.ProductMapper;
import com.ecommerce.product_service.repository.CategoryRepository;
import com.ecommerce.product_service.repository.ProductRepository;
import com.ecommerce.product_service.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductMapper productMapper;
    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    @Override
    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProduct(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        return productMapper.toProductResponse(product);
    }

    @Override
    public PageResponse<ProductResponse> getProducts(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductResponse> productResponses = productPage.getContent().stream().map(productMapper::toProductResponse).toList();
        return PageResponse.<ProductResponse>builder()
                .currentPage(pageNum)
                .pageSize(pageSize)
                .totalPages(productPage.getTotalPages())
                .totalElements(productPage.getTotalElements())
                .data(productResponses)
                .build();
    }

    @Override
    @CachePut(cacheNames = "products", key = "#result.id")
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = productMapper.toProduct(productRequest);
        Set<Category> categories = new HashSet<>();
        for(String categoryId : productRequest.getCategoryIds()){
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
            categories.add(category);
        }
        product.setCategories(categories);
        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Override
    @CachePut(cacheNames = "products", key = "#id")
    public ProductResponse updateProduct(String id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        Set<Category> categories = new HashSet<>();
        for(String categoryId : productRequest.getCategoryIds()){
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
            categories.add(category);
        }
        product.setCategories(categories);
        productMapper.updateProduct(product, productRequest);
        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Override
    @CacheEvict(cacheNames = "products", key = "#id", beforeInvocation = true)
    public String deleteProduct(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        productRepository.delete(product);
        return  "Deleted product with ID " + product.getId();
    }
}
