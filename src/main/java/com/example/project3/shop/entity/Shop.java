package com.example.project3.shop.entity;


import com.example.project3.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String category;

    @Enumerated(EnumType.STRING)
    private com.example.project3.shop.enums.ShopStatus status;

    @ManyToOne
    private UserEntity owner;

    private String rejectionReason;
    private String closureReason;


    @OneToMany(mappedBy = "shop")
    private List<com.example.project3.shop.entity.Product> products;

    private LocalDateTime lastTransactionDate; // 최근 거래 시간



    public void setCategory(com.example.project3.shop.enums.ShopCategory category) {
    }

    public void setRejectionReason(String reason) {
    }

    public void setClosureReason(String reason) {
    }
}
