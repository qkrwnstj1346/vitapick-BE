package com.vita.vitapickBack.useraddr;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class UserAddrController {

    private final UserAddrService userAddrService;

    // 회원 배송지 목록 조회
    // 마이페이지 배송지 관리 / 주문서 배송지 선택 둘 다 사용
    @GetMapping
    public ResponseEntity<?> findByUserNum(@RequestParam("userNum") Long userNum) {
        try {
            List<UserAddr> result = userAddrService.findByUserNum(userNum);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("배송지 목록 조회에 실패했습니다.");
        }
    }

    // 배송지 등록
    // 마이페이지 배송지 추가 / 주문서에서 배송지 없을 때 추가 둘 다 사용
    @PostMapping
    public ResponseEntity<?> createAddr(@RequestBody UserAddrDTO dto) {
        try {
            UserAddr result = userAddrService.createAddr(dto);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("배송지 등록에 실패했습니다.");
        }
    }

    // 배송지 수정
    // 마이페이지 배송지 수정 / 주문서에서 배송지 변경 시 사용 가능
    @PatchMapping("/{addrId}")
    public ResponseEntity<?> updateAddr(@PathVariable("addrId") Long addrId, @RequestBody UserAddrDTO dto) {
        try {
            UserAddr result = userAddrService.updateAddr(addrId, dto);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("배송지 수정에 실패했습니다.");
        }
    }

    // 배송지 삭제
    // 마이페이지 배송지 관리에서 사용
    @DeleteMapping("/{addrId}")
    public ResponseEntity<?> deleteAddr(@PathVariable("addrId") Long addrId) {
        try {
            userAddrService.deleteAddr(addrId);
            return ResponseEntity.status(HttpStatus.OK).body("배송지가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("배송지 삭제에 실패했습니다.");
        }
    }

    // 기본 배송지 변경
    // 마이페이지 기본배송지 설정 / 주문서에서 선택 배송지를 기본배송지로 변경할 때 사용
    @PatchMapping("/{addrId}/base")
    public ResponseEntity<?> updateBaseAddr(
            @PathVariable("addrId") Long addrId,
            @RequestParam("userNum") Long userNum) {
        try {
            userAddrService.updateBaseAddr(userNum, addrId);
            return ResponseEntity.status(HttpStatus.OK).body("기본 배송지가 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("기본 배송지 변경에 실패했습니다.");
        }
    }
}