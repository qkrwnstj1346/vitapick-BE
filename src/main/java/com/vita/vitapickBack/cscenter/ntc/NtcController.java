package com.vita.vitapickBack.cscenter.ntc;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/cscenter")
@Log4j2
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174" })
public class NtcController {

    private final NtcService ntcService;

    // 공지사항 전체 목록 조회(관리자)
    @GetMapping("/admin/notices")
    public ResponseEntity<?> allntclist() {

        try {
            List<Ntc> result = ntcService.allNtcList();

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("목록조회실패");
        }
    }

    // use_yn = 'Y' 공지사항 목록 조회(회원)
    @GetMapping("/notices")
    public ResponseEntity<?> useyntcList() {

        try {
            List<Ntc> result = ntcService.UseYNtcList('Y');

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("목록조회실패");
        }
    }

    // 공지사항 상세 조회
    @GetMapping("/notices/{ntcId}")
    public ResponseEntity<?> ntcdetail(@PathVariable("ntcId") Long ntcId) {

        try {
            Ntc entity = ntcService.selectOne(ntcId);

            return ResponseEntity.status(HttpStatus.OK).body(entity);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("해당하는 번호는 존재하지 않습니다.");
        }
    }

    // 공지사항 등록(관리자)
    @PostMapping("/admin/notices")
    public ResponseEntity<?> insert(@RequestBody Ntc entity) {

        try {
            Ntc result = ntcService.saveNtc(entity);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("입력오류입니다.");
        }
    }

    // 공지사항 수정(관리자)
    @PatchMapping("/admin/notices/{ntcId}")
    public ResponseEntity<?> update(
            @PathVariable("ntcId") Long ntcId,
            @RequestBody Ntc entity) {

        try {
            Ntc result = ntcService.updateNtc(ntcId, entity);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("수정실패");
        }
    }

    // 공지사항 삭제(관리자)
    @DeleteMapping("/admin/notices/{ntcId}")
    public ResponseEntity<?> deleteNotice(@PathVariable("ntcId") Long ntcId) {

        try {
            ntcService.deleteNtc(ntcId);

            return ResponseEntity.ok("공지사항 삭제 성공");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}