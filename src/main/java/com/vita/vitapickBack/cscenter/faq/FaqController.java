package com.vita.vitapickBack.cscenter.faq;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FaqController {

	private final FaqService faqService;

	// FAQ 전체 목록 조회
	@GetMapping("/faqs")
	public ResponseEntity<?> findFaqAll(
			@RequestParam(value = "faqCtgCd", required = false) String faqCtgCd) {

		try {

			List<Faq> result;

			// 카테고리별 조회
			if (faqCtgCd != null) {

				result = faqService.findByFaqCtgCd(faqCtgCd);

			} else {

				result = faqService.findFaqAll();
			}

			return ResponseEntity
					.status(HttpStatus.OK)
					.body(result);

		} catch (Exception e) {

			return ResponseEntity
					.status(HttpStatus.BAD_GATEWAY)
					.body("FAQ 목록 조회에 실패했습니다.");
		}
	}

	// use_yn = 'Y' FAQ 조회
	@GetMapping("/faqs/useyn/{useYn}")
	public ResponseEntity<?> findByUseYn(@PathVariable("useYn") String useYn) {

		try {

			List<Faq> result = faqService.findByUseYn(useYn);

			return ResponseEntity
					.status(HttpStatus.OK)
					.body(result);

		} catch (Exception e) {

			return ResponseEntity
					.status(HttpStatus.BAD_GATEWAY)
					.body("FAQ 목록 조회에 실패했습니다.");
		}
	}

	// FAQ 상세 조회
	@GetMapping("/faqs/{faqId}")
	public ResponseEntity<?> selectOne(@PathVariable("faqId") Long faqId) {

		try {

			Faq result = faqService.selectOne(faqId);

			return ResponseEntity
					.status(HttpStatus.OK)
					.body(result);

		} catch (Exception e) {

			return ResponseEntity
					.status(HttpStatus.BAD_GATEWAY)
					.body("FAQ 상세 정보를 불러오지 못했습니다.");
		}
	}

	// FAQ 등록
	@PostMapping("/faqs")
	public ResponseEntity<?> saveFaq(@RequestBody FaqDto dto) {

		try {

			Faq result = faqService.saveFaq(dto);

			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(result);

		} catch (Exception e) {

			return ResponseEntity
					.status(HttpStatus.BAD_GATEWAY)
					.body("FAQ 등록에 실패했습니다.");
		}
	}

	// FAQ 수정
	@PatchMapping("/faqs/{faqId}")
	public ResponseEntity<?> updateFaq(
			@PathVariable("faqId") Long faqId,
			@RequestBody FaqDto dto) {

		try {

			Faq result = faqService.updateFaq(faqId, dto);

			return ResponseEntity
					.status(HttpStatus.OK)
					.body(result);

		} catch (Exception e) {

			return ResponseEntity
					.status(HttpStatus.BAD_GATEWAY)
					.body("FAQ 수정에 실패했습니다.");
		}
	}

	// FAQ 삭제
	@DeleteMapping("/faqs/{faqId}")
	public ResponseEntity<?> deleteFaq(@PathVariable("faqId") Long faqId) {

		try {

			faqService.deleteFaq(faqId);

			return ResponseEntity
					.status(HttpStatus.OK)
					.body("삭제완료");

		} catch (Exception e) {

			return ResponseEntity
					.status(HttpStatus.BAD_GATEWAY)
					.body("FAQ 삭제에 실패했습니다.");
		}
	}

}