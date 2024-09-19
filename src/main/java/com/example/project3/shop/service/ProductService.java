package com.example.project3.shop.service;

import com.example.project3.shop.entity.Shop;
import com.example.project3.shop.dto.ProductDTO;
import com.example.project3.shop.entity.Product;
import com.example.project3.shop.exception.ProductNotFoundException;
import com.example.project3.shop.repo.ProductRepository;
import com.example.project3.shop.repo.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    public ProductService(ProductRepository productRepository, ShopRepository shopRepository) {
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
    }

    public Product createProduct(Long shopId, ProductDTO productDTO) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImageUrl(productDTO.getImageUrl());
        product.setShop(shop);

        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImageUrl(productDTO.getImageUrl());

        return productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    // 핑몰 상품 검색

    public List<ProductDTO> searchProducts(String name, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> products = productRepository.findByNameAndPriceRange(name, minPrice, maxPrice);
        return products.stream()
                .map(product -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setName(product.getName());
                    dto.setImageUrl(product.getImageUrl());
                    dto.setDescription(product.getDescription());
                    dto.setPrice(product.getPrice());
                    dto.setStock(product.getStock());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 쇼핑몰 상품 구매
    public void updateStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    public Product getProductById(Long productId) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            return product.get();
        } else {
            throw new ProductNotFoundException("Product with ID " + productId + " not found.");
        }
    }
}
