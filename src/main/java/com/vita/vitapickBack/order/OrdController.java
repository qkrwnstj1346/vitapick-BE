package com.vita.vitapickBack.order;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrdController {

	private final OrdService ordService;

	// 회원 주문 목록 조회
	@GetMapping
	public ResponseEntity<?> findByUserNum(@RequestParam("userNum") Long userNum) {
		try {
			List<Ord> result = ordService.findByUserNum(userNum);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("주문 내역 조회에 실패했습니다.");
		}
	}

	// 주문번호 조회
	@GetMapping("/{ordNo}")
	public ResponseEntity<?> findByOrdNo(@PathVariable("ordNo") String ordNo) {
		try {
			Ord result = ordService.findByOrdNo(ordNo);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("주문 조회에 실패했습니다.");
		}
	}

	// 주문별 주문상품 목록 조회
	@GetMapping("/{ordId}/items")
	public ResponseEntity<?> findOrdItByOrdId(@PathVariable("ordId") Long ordId) {
		try {
			List<OrdIt> result = ordService.findOrdItByOrdId(ordId);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("주문상품 목록 조회에 실패했습니다.");
		}
	}

	// 상품별 주문 조회
	@GetMapping("/product/{prdId}")
	public ResponseEntity<?> findByPrdId(@PathVariable("prdId") Long prdId) {
		try {
			List<OrdIt> result = ordService.findByPrdId(prdId);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("상품별 주문 조회에 실패했습니다.");
		}
	}

	// 주문번호로 결제 조회
	@GetMapping("/{ordId}/pay")
	public ResponseEntity<?> findPayByOrdId(@PathVariable("ordId") Long ordId) {
		try {
			Pay result = ordService.findPayByOrdId(ordId);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("결제 정보 조회에 실패했습니다.");
		}
	}

	// 결제번호 조회
	@GetMapping("/pay/{payNo}")
	public ResponseEntity<?> findByPayNo(@PathVariable("payNo") String payNo) {
		try {
			Pay result = ordService.findByPayNo(payNo);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("결제 정보 조회에 실패했습니다.");
		}
	}

	// 주문 생성 + 결제
	@PostMapping
	public ResponseEntity<?> createOrder(@RequestBody OrdDTO orddto) {
		try {
			Ord result = ordService.createOrder(orddto);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			log.error("주문 생성 및 결제 실패", e);
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("결제에 실패했습니다.");
		}
	}

	// 주문 완료 페이지
	@GetMapping("/complete/{ordNo}")
	public ResponseEntity<?> orderComplete(@PathVariable("ordNo") String ordNo) {
		try {
			Ord result = ordService.findByOrdNo(ordNo);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("주문 완료 정보 조회에 실패했습니다.");
		}
	}
}