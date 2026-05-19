package com.vita.vitapickBack.products.prd;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}