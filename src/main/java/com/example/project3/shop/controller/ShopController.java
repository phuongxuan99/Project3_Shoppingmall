package com.example.project3.shop.controller;

import com.example.project3.shop.dto.ShopUpdateDTO;
import com.example.project3.shop.entity.Shop;
import com.example.project3.shop.enums.ShopCategory;
import com.example.project3.shop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shops")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    // 쇼핑몰 생성
    @PostMapping
    public ResponseEntity<Shop> createShop(@RequestBody Long userId) {
        return ResponseEntity.ok(shopService.createShop(userId));
    }

    // 쇼핑몰 업데이터
    @PutMapping("/{id}")
    public ResponseEntity<Shop> updateShop(@PathVariable Long id, @RequestBody ShopUpdateDTO dto) {
        return ResponseEntity.ok(shopService.updateShop(id, dto));
    }

    // 쇼핑몰 개설 요청
    @PostMapping("/{id}/request-approval")
    public ResponseEntity<Void> requestApproval(@PathVariable Long id) {
        shopService.requestApproval(id);
        return ResponseEntity.ok().build();
    }

    // 쇼핑몰 승인
    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveShop(@PathVariable Long id) {
        shopService.approveShop(id);
        return ResponseEntity.ok().build();
    }

    //쇼핑몰 거부
    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectShop(@PathVariable Long id, @RequestBody String reason) {
        shopService.rejectShop(id, reason);
        return ResponseEntity.ok().build();
    }

    // 쇼핑몰 폐쇄 요청
    @PostMapping("/{id}/request-closure")
    public ResponseEntity<Void> requestClosure(@PathVariable Long id, @RequestBody String reason) {
        shopService.requestClosure(id, reason);
        return ResponseEntity.ok().build();
    }

    // 쇼핑몰 폐쇄
    @PostMapping("/{id}/close")
    public ResponseEntity<Void> closeShop(@PathVariable Long id) {
        shopService.closeShop(id);
        return ResponseEntity.ok().build();
    }

    ///쇼핑몰 조회
    @GetMapping
    public ResponseEntity<List<Shop>> getAllShops() {
        List<Shop> shops = shopService.getAllShops();
        return ResponseEntity.ok(shops);
    }

    // 쇼핑몰 검색
    @GetMapping("/search")
    public ResponseEntity<List<Shop>> searchShops(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String category) {
        List<Shop> shops = shopService.searchShops(name, category);
        return ResponseEntity.ok(shops);
    }



}
