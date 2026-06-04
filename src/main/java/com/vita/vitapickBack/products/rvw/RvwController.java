package com.vita.vitapickBack.products.rvw;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rvw")
public class RvwController {

    // 리뷰 관련 비즈니스 로직을 처리하는 서비스
    private final RvwService rvwService;

    // 리뷰 작성
    @PostMapping
    public ResponseEntity<Rvw> createRvw(
            @AuthenticationPrincipal Long userNum,
            @RequestBody RvwDTO dto) {

        // userNum은 토큰에서 가져오고, 리뷰 내용은 요청 바디에서 받음
        return ResponseEntity.ok(rvwService.createRvw(userNum, dto));
    }

    // 상품 ID로 리뷰 목록 조회
    @GetMapping("/prd/{prdId}")
    public ResponseEntity<List<Rvw>> findByPrdId(
            @PathVariable("prdId") Long prdId) {

        return ResponseEntity.ok(rvwService.findByPrdId(prdId));
    }

    // 로그인한 사용자의 리뷰 목록 조회
    @GetMapping("/user")
    public ResponseEntity<List<Rvw>> findByUserNum(
            @AuthenticationPrincipal Long userNum) {

        return ResponseEntity.ok(rvwService.findByUserNum(userNum));
    }

    // 리뷰 단건 조회
    @GetMapping("/{rvwId}")
    public ResponseEntity<Rvw> findByRvwId(
            @PathVariable("rvwId") Long rvwId) {

        return ResponseEntity.ok(rvwService.findByRvwId(rvwId));
    }

    // 리뷰 삭제
    // 실제 DB 삭제가 아니라 useYn을 N으로 바꾸는 소프트 삭제
    @PatchMapping("/{rvwId}/cancel")
    public ResponseEntity<Void> cancelRvw(
            @AuthenticationPrincipal Long userNum,
            @PathVariable("rvwId") Long rvwId) {

        rvwService.cancelRvw(userNum, rvwId);
        return ResponseEntity.ok().build();
    }
}