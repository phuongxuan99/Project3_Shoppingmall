package com.example.project3.shop.repo;

import com.example.project3.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //핑몰 상품 검색
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByNameAndPriceRange(
            @Param("name") String name,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice);
}
