package com.vita.vitapickBack.products.rvw;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rvw")
public class RvwController {

    private final RvwService rvwService;

    // 리뷰 작성
    @PostMapping
    public ResponseEntity<Rvw> createRvw(@RequestBody RvwDTO dto) {
        return ResponseEntity.ok(rvwService.createRvw(dto));
    }
    
    // 상품 ID로 리뷰 전체 조회
    @GetMapping("/prd/{prdId}")
    public ResponseEntity<List<Rvw>> findByPrdId(@PathVariable("prdId") Long prdId) {
        return ResponseEntity.ok(rvwService.findByPrdId(prdId));
    }
   
}