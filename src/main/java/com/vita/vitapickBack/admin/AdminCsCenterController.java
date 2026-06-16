package com.vita.vitapickBack.admin;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vita.vitapickBack.cscenter.faq.Faq;
import com.vita.vitapickBack.cscenter.faq.FaqService;
import com.vita.vitapickBack.cscenter.ntc.Ntc;
import com.vita.vitapickBack.cscenter.ntc.NtcService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cscenter")
public class AdminCsCenterController {

    private final NtcService ntcService;
    private final FaqService faqService;
    private final AdminCsInquiriesService adminCsInquiriesService;

    @GetMapping("/notices")
    public ResponseEntity<Page<Ntc>> getNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String useYn,
            @RequestParam(defaultValue = "latest") String sort) {
        return ResponseEntity.ok(ntcService.findAdminNtcPage(page, size, useYn, sort));
    }

    @GetMapping("/faqs")
    public ResponseEntity<Page<Faq>> getFaqs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String useYn,
            @RequestParam(defaultValue = "latest") String sort) {
        return ResponseEntity.ok(faqService.findAdminFaqPage(page, size, useYn, sort));
    }

    @GetMapping("/inquiries")
    public ResponseEntity<AdminCsInquiriesResponseDTO> getInquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(adminCsInquiriesService.getInquiries(page, size, keyword, status, type, startDate, endDate));
    }
}
