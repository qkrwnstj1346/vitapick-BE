package com.vita.vitapickBack.cart;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("장바구니 목록 조회에 실패했습니다.");
		}
	}

	// 동일 상품 체크
	@GetMapping("/check/{userNum}/{prdId}")
	public ResponseEntity<?> findByUserNumAndPrdId(
			@PathVariable("userNum") Long userNum,
			@PathVariable("prdId") Long prdId) {
		try {
			Cart result = cartService.findByUserNumAndPrdId(userNum, prdId);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("동일 상품 확인에 실패했습니다.");
		}
	}

	// 장바구니 담기
	@PostMapping
	public ResponseEntity<?> addCart(@RequestBody CartDTO dto) {
		try {
			Cart result = cartService.addCart(dto);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
		}
	}

	// 장바구니 수량 증가/감소/변경
	@PatchMapping("/{cartId}/qty")
	public ResponseEntity<?> updateQty(
			@PathVariable("cartId") Long cartId,
			@RequestBody CartDTO dto) {
		try {
			Cart result = cartService.updateQty(cartId, dto.getItQty());
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
		}
	}

	// 장바구니 개별 삭제
	@DeleteMapping("/{cartId}")
	public ResponseEntity<?> deleteCart(@PathVariable("cartId") Long cartId) {
		try {
			Cart result = cartService.deleteCart(cartId);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
		}
	}

	// 선택된 장바구니 상품 조회
	@GetMapping("/selected/{userNum}")
	public ResponseEntity<?> findByUserNumAndSelectedYn(@PathVariable("userNum") Long userNum) {
		try {
			List<Cart> result = cartService.findByUserNumAndSelectedYn(userNum, 'Y');
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("선택 상품 조회에 실패했습니다.");
		}
	}

	// 선택 상품 삭제
	@DeleteMapping("/selected/{userNum}")
	public ResponseEntity<?> deleteByUserNumAndSelectedYn(@PathVariable("userNum") Long userNum) {
		try {
			cartService.deleteByUserNumAndSelectedYn(userNum, 'Y');
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("선택 상품 삭제에 실패했습니다.");
		}
	}

	// 전체 삭제
	@DeleteMapping("/all/{userNum}")
	public ResponseEntity<?> deleteByUserNum(@PathVariable("userNum") Long userNum) {
		try {
			cartService.deleteByUserNum(userNum);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("장바구니 전체 삭제에 실패했습니다.");
		}
	}

	// 개별 수량 최대 10개 체크
	@GetMapping("/check/qty/{itQty}")
	public ResponseEntity<?> checkQty(@PathVariable("itQty") Integer itQty) {
		try {
			cartService.checkQty(itQty);
			return ResponseEntity.status(HttpStatus.OK).body("수량 체크 성공");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
		}
	}

	// 장바구니 전체 수량 99개 체크
	@GetMapping("/check/total/{userNum}/{itQty}")
	public ResponseEntity<?> totalCheckQty(
			@PathVariable("userNum") Long userNum,
			@PathVariable("itQty") Integer itQty) {
		try {
			cartService.totalCheckQty(userNum, itQty);
			return ResponseEntity.status(HttpStatus.OK).body("전체 수량 체크 성공");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
		}
	}
}