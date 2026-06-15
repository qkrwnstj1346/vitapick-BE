package com.vita.vitapickBack.admin;

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
public class AdminReviewsResponseDTO {

    private List<AdminReviewDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminReviewDTO {
        private Long reviewId;
        private Long productId;
        private String productName;
        private String writerId;
        private String writerName;
        private Integer rating;
        private String content;
        private String useYn;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
