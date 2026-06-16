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
public class AdminOrdersResponseDTO {

    private List<AdminOrderDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminOrderDTO {
        private Long orderId;
        private String orderNo;
        private String productName;
        private String buyerId;
        private String buyerName;
        private Integer totalPrice;
        private String orderStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
