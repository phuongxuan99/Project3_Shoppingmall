package com.example.project3.shop.service;

import com.example.project3.user.repo.UserRepository;
import com.example.project3.user.entity.UserEntity;
import com.example.project3.shop.dto.ShopUpdateDTO;
import com.example.project3.shop.entity.Shop;
import com.example.project3.shop.enums.ShopStatus;
import com.example.project3.shop.repo.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShopService {
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;


    public ShopService(ShopRepository shopRepository, UserRepository userRepository) {
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
    }

    public Shop createShop(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isBusiness()) {
            user.setBusiness(true);
            userRepository.save(user);
        }

        Shop shop = new Shop();
        shop.setOwner(user);
        shop.setStatus(ShopStatus.PREPARING);
        return shopRepository.save(shop);

    }

    public Shop updateShop(Long shopId, ShopUpdateDTO dto) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        shop.setName(dto.getName());
        shop.setDescription(dto.getDescription());
        shop.setCategory(dto.getCategory());
        return shopRepository.save(shop);
    }

    @Transactional
    public void requestApproval(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        if (shop.getName() != null && shop.getDescription() != null && shop.getCategory() != null) {
            shop.setStatus(ShopStatus.PENDING_APPROVAL);
            shopRepository.save(shop);
        } else {
            throw new RuntimeException("Shop information is incomplete");
        }
    }

    public void approveShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        shop.setStatus(ShopStatus.OPEN);
        shopRepository.save(shop);
    }

    public void rejectShop(Long shopId, String reason) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        shop.setStatus(ShopStatus.PREPARING);
        shop.setRejectionReason(reason);
        shopRepository.save(shop);
    }

    public void requestClosure(Long shopId, String reason) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        shop.setClosureReason(reason);
        shopRepository.save(shop);
    }

    public void closeShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        shop.setStatus(ShopStatus.CLOSED);
        shopRepository.save(shop);
    }


    //쇼핑몰 조회
    public List<Shop> getAllShops() {
        return shopRepository.findAllOrderByLatestTransaction();
    }

    public List<Shop> searchShops(String name, String category) {
        return shopRepository.findByNameOrCategory(name, category);
    }
}
