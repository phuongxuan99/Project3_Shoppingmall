package com.example.project3.shop.controller;

import com.example.project3.shop.dto.ProductDTO;
import com.example.project3.shop.entity.Product;
import com.example.project3.shop.service.ProductService;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/shops/{shopId}/products")

public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 상품 생성
    @PostMapping
    public ResponseEntity<Product> createProduct(@PathVariable Long shopId, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.createProduct(shopId, productDTO));
    }

    // 상풍 업데이터
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(productId, productDTO));
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    // 상품 검색
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(
            @RequestParam String name,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<ProductDTO> products = productService.searchProducts(name, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }


}
