package com.vita.vitapickBack.admin.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vita.vitapickBack.admin.dto.AdminCsInquiriesResponseDTO;
import com.vita.vitapickBack.admin.service.AdminCsCenterService;
import com.vita.vitapickBack.admin.service.AdminCsInquiriesService;
import com.vita.vitapickBack.cscenter.faq.Faq;
import com.vita.vitapickBack.cscenter.ntc.Ntc;
import com.vita.vitapickBack.cscenter.ntc.NtcService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cscenter")
public class AdminCsCenterController {

    private final NtcService ntcService;
    private final AdminCsCenterService adminCsCenterService;
    private final AdminCsInquiriesService adminCsInquiriesService;

    @GetMapping("/notices")
    public ResponseEntity<Page<Ntc>> getNotices(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "useYn", required = false) String useYn,
            @RequestParam(name = "sort", defaultValue = "latest") String sort) {
        return ResponseEntity.ok(ntcService.findAdminNtcPage(page, size, useYn, sort));
    }

    @GetMapping("/faqs")
    public ResponseEntity<Page<Faq>> getFaqs(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "useYn", required = false) String useYn,
            @RequestParam(name = "faqCtgCd", required = false) String faqCtgCd,
            @RequestParam(name = "sort", defaultValue = "latest") String sort) {
        return ResponseEntity.ok(adminCsCenterService.getFaqs(page, size, useYn, faqCtgCd, sort));
    }

    @GetMapping("/inquiries")
    public ResponseEntity<AdminCsInquiriesResponseDTO> getInquiries(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(adminCsInquiriesService.getInquiries(page, size, keyword, status, type, startDate, endDate));
    }
}
