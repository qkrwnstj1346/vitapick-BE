package com.vita.vitapickBack.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vita.vitapickBack.admin.dto.AdminProductsResponseDTO;
import com.vita.vitapickBack.admin.service.AdminProductsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class AdminProductsController {

    private final AdminProductsService adminProductsService;

    // 관리자 상품 목록 조회 API
    @GetMapping
    public ResponseEntity<AdminProductsResponseDTO> getProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "categoryId", required = false) Integer categoryId) {

        return ResponseEntity.ok(adminProductsService.getProducts(page, size, keyword, status, categoryId));
    }
}
