package com.vita.vitapickBack.admin.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vita.vitapickBack.admin.dto.AdminReviewsResponseDTO;
import com.vita.vitapickBack.admin.dto.AdminReviewsResponseDTO.AdminReviewDTO;
import com.vita.vitapickBack.admin.dto.AdminReviewsResponseDTO.AdminReviewReplyRequestDTO;
import com.vita.vitapickBack.admin.service.AdminReviewsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reviews")
public class AdminReviewsController {

    private final AdminReviewsService adminReviewsService;

    @GetMapping
    public ResponseEntity<AdminReviewsResponseDTO> getReviews(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "rating", required = false) Integer rating,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(adminReviewsService.getReviews(page, size, keyword, rating, startDate, endDate));
    }

    @GetMapping("/{rvwId}")
    public ResponseEntity<AdminReviewDTO> getReview(@PathVariable("rvwId") Long rvwId) {
        return ResponseEntity.ok(adminReviewsService.getReview(rvwId));
    }

    @PatchMapping("/{rvwId}/reply")
    public ResponseEntity<AdminReviewDTO> saveReply(
            @PathVariable("rvwId") Long rvwId,
            @RequestBody AdminReviewReplyRequestDTO request) {
        return ResponseEntity.ok(adminReviewsService.saveReply(rvwId, request));
    }

    @DeleteMapping("/{rvwId}/reply")
    public ResponseEntity<AdminReviewDTO> deleteReply(@PathVariable("rvwId") Long rvwId) {
        return ResponseEntity.ok(adminReviewsService.deleteReply(rvwId));
    }
}
