package com.vita.vitapickBack.products.prd;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class PrdController {

    private final PrdService prdService;

    @GetMapping("/list")
    public ResponseEntity<List<Prd>> getAllPrd() {
        return ResponseEntity.ok(prdService.getAllPrd());
    }
    
    // 상품 + 이미지 같이 조회
    @GetMapping("/list/img")
    public ResponseEntity<List<PrdDTO>> getAllPrdWithImg() {
        return ResponseEntity.ok(prdService.getAllPrdWithImg());
    }
    
    @GetMapping("/list/category/{catCd}")
    public ResponseEntity<List<PrdDTO>> getPrdByCategory(@PathVariable("catCd") int catCd) {
        return ResponseEntity.ok(prdService.getPrdByCategory(catCd));
    }
}