package com.vita.vitapickBack.admin.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCsInquiriesResponseDTO {

    private List<AdminCsInquiryDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminCsInquiryDTO {
        private Long inquiryId;
        private String title;
        private String writerId;
        private String writerName;
        private String category;
        private String status;
        private Integer viewCnt;
        private LocalDateTime answeredAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
