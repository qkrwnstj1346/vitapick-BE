package com.vita.vitapickBack.cscenter.inq;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
public class InqController {

	private final InqService inqService;

	// 전체 문의 목록 조회
	@GetMapping("/inquiries")
	public ResponseEntity<?> getAllInq() {

		try {

			List<Inq> result = inqService.getAllInq();

			return ResponseEntity.status(HttpStatus.OK).body(result);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("1:1 문의 목록 조회에 실패했습니다.");
		}
	}

	// 회원 본인 문의 목록 조회 (마이페이지)
	@GetMapping("/mypage/inquiries/{userNum}")
	public ResponseEntity<?> getMyInq(@PathVariable("userNum") Long userNum) {

		try {

			List<Inq> result = inqService.getMyInq(userNum);

			return ResponseEntity.status(HttpStatus.OK).body(result);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("내 문의 목록 조회에 실패했습니다.");
		}
	}

	// 관리자 문의 상세 조회
	@GetMapping("/inquiries/{inqId}")
	public ResponseEntity<?> selectOne(@PathVariable("inqId") Long inqId) {

		try {

			Inq result = inqService.selectOne(inqId);

			return ResponseEntity.status(HttpStatus.OK).body(result);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("문의 상세 정보를 불러오지 못했습니다.");
		}
	}

	// 회원 본인 문의 상세 조회
	@GetMapping("/inquiries/{inqId}/{userNum}")
	public ResponseEntity<?> selectMyOne(@PathVariable("inqId") Long inqId, @PathVariable("userNum") Long userNum) {

		try {

			Inq result = inqService.selectMyOne(inqId, userNum);

			return ResponseEntity.status(HttpStatus.OK).body(result);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("문의 상세 정보를 불러오지 못했습니다.");
		}
	}

	// 문의 등록
	@PostMapping("/inquiries")
	public ResponseEntity<?> createInq(@RequestBody Inq inq) {

		try {

			inqService.createInq(inq);

			return ResponseEntity.status(HttpStatus.CREATED).build();

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("1:1 문의 등록에 실패했습니다.");
		}
	}

	// 회원 본인 문의 수정
	@PatchMapping("/inquiries/{inqId}/{userNum}")
	public ResponseEntity<?> updateInq(@PathVariable("inqId") Long inqId, @PathVariable("userNum") Long userNum,
			@RequestBody Inq inq) {

		try {

			inqService.updateInq(inqId, userNum, inq);

			return ResponseEntity.status(HttpStatus.OK).build();

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("1:1 문의 수정에 실패했습니다.");
		}
	}

	// 회원 본인 문의 삭제
	@DeleteMapping("/inquiries/{inqId}/{userNum}")
	public ResponseEntity<?> deleteInq(@PathVariable("inqId") Long inqId, @PathVariable("userNum") Long userNum) {

		try {

			inqService.deleteInq(inqId, userNum);

			return ResponseEntity.status(HttpStatus.OK).build();

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("1:1 문의 삭제에 실패했습니다.");
		}
	}

	// 관리자 답변 등록
	@PatchMapping("/inquiries/{inqId}/answer")
	public ResponseEntity<?> answerInq(@PathVariable("inqId") Long inqId, @RequestBody Inq inq) {

		try {

			inqService.answerInq(inqId, inq.getAnsTxt());

			return ResponseEntity.status(HttpStatus.OK).build();

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("댓글 등록에 실패했습니다.");
		}
	}

}