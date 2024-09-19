package com.example.project3.shop.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String imageUrl;
    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private int stock;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;


}
