package com.example.project3.shop.dto;

import com.example.project3.shop.enums.ShopCategory;
import lombok.Data;

@Data
public class ShopUpdateDTO {
    private Long id;
    private String name;
    private String description;
    private ShopCategory category;


}
