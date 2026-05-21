package com.vita.vitapickBack.cart;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
	
	private final CartService cartService;
	
	// 회원 장바구니 목록 조회
	@GetMapping("/{userNum}")
	public ResponseEntity<?> findByUserNum(@PathVariable("userNum") Long userNum) {
		try {
			List<Cart> result = cartService.findByUserNum(userNum);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("장바구니에 담긴 상품이 없습니다.");
		}
	}
	
	// 동일 상품 체크
	
	// 장바구니 담기
	
	// 장바구니 수량 증가/감소/변경
	
	// 장바구니 개별 삭제
	
	// 선택된 장바구니 상품 조회
	
	// 선택 상품 삭제
	
	// 전체 삭제
	
	// 개별 수량 최대 10개 체크
	
	// 장바구니 전체 수량 99개 체크

}
