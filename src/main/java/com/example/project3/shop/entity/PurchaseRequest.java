package com.example.project3.shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
public class PurchaseRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long productId;
    private int quantity;
    private BigDecimal totalAmount;
    private boolean isAccepted;
    private boolean isCancelled;



}
