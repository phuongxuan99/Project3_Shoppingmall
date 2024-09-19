package com.example.project3.shop.repo;

import com.example.project3.shop.entity.Shop;
import com.example.project3.shop.enums.ShopStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Query("SELECT s FROM Shop s WHERE s.status = 'OPEN' ORDER BY s.lastTransactionDate DESC")
    List<Shop> findAllOrderByLatestTransaction();

    @Query("SELECT s FROM Shop s WHERE s.status = 'OPEN' AND (s.name LIKE %:name% OR s.category LIKE %:category%) ORDER BY s.lastTransactionDate DESC")
    List<Shop> findByNameOrCategory(
            @Param("name") String name,
            @Param("category") String category);
}