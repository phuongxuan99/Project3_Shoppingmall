package com.example.project3.shop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;

}
