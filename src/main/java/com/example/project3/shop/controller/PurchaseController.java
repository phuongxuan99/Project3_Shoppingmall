package com.example.project3.shop.controller;


import com.example.project3.shop.entity.PurchaseRequest;
import com.example.project3.shop.service.PurchaseService;
import jakarta.persistence.Table;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop/purchase-requests")
@Table(name = "purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }


    // 구매 요청 생성
    @PostMapping
    public PurchaseRequest createPurchaseRequest(@RequestBody PurchaseRequest request) {
        return purchaseService.createPurchaseRequest(
                request.getUserId(), request.getProductId(), request.getQuantity(), request.getTotalAmount());
    }

    // 구매 요청 수락
    @PostMapping("/{id}/accept")
    public void acceptPurchaseRequest(@PathVariable Long id) {
        purchaseService.acceptPurchaseRequest(id);
    }

    // 구매 요청 취소
    @PostMapping("/{id}/cancel")
    public void cancelPurchaseRequest(@PathVariable Long id) {
        purchaseService.cancelPurchaseRequest(id);
    }
}