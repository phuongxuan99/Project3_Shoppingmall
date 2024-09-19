package com.example.project3.shop.service;

import com.example.project3.user.repo.UserRepository;
import com.example.project3.shop.entity.PurchaseRequest;
import com.example.project3.shop.repo.PurchaseRequestRepository;
import com.example.project3.shop.repo.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PurchaseService {
    private final ProductRepository productRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    public PurchaseService(ProductRepository productRepository, PurchaseRequestRepository purchaseRequestRepository, UserRepository userRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.userRepository = userRepository;
        this.productService = productService;
    }

    public PurchaseRequest createPurchaseRequest(Long userId, Long productId, int quantity, BigDecimal totalAmount) {
        PurchaseRequest request = new PurchaseRequest();
        request.setUserId(userId);
        request.setProductId(productId);
        request.setQuantity(quantity);
        request.setTotalAmount(totalAmount);
        request.setAccepted(false);
        request.setCancelled(false);

        return purchaseRequestRepository.save(request);
    }

    public void acceptPurchaseRequest(Long requestId) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));

        if (request.isAccepted() || request.isCancelled()) {
            throw new RuntimeException("Request has already been processed");
        }

        // Update stock
        productService.updateStock(request.getProductId(), request.getQuantity());

        request.setAccepted(true);
        purchaseRequestRepository.save(request);
    }

    public void cancelPurchaseRequest(Long requestId) {
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));

        if (request.isAccepted() || request.isCancelled()) {
            throw new RuntimeException("Request has already been processed");
        }

        request.setCancelled(true);
        purchaseRequestRepository.save(request);
    }


}