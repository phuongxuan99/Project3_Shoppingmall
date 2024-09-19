package com.example.project3.shop.repo;

import com.example.project3.user.entity.UserEntity;
import com.example.project3.shop.entity.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    List<PurchaseRequest> findByIdAndUserId(Long id, Long userId);

}