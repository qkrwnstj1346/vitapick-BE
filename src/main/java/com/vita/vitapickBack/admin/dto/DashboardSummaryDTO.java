package com.vita.vitapickBack.admin.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {

    private Long todaySalesAmt;
    private Long monthSalesAmt;
    private Long todayPaidOrderCount;
    private PopularCategoryDTO popularCategory;
    private List<ProductSalesTopDTO> productSalesTop5;
    private InquiryStatsDTO inquiryStats;
    private MemberStatsDTO memberStats;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopularCategoryDTO {
        private Integer catCd;
        private String catNm;
        private Long salesAmt;
        private Long orderQty;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSalesTopDTO {
        private Long prdId;
        private String prdNm;
        private Integer catCd;
        private String catNm;
        private Long paidQty;
        private Long salesAmt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquiryStatsDTO {
        private Long waitingCount;
        private Long answeredCount;
        private Long todayNewCount;
        private Double answerRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberStatsDTO {
        private Long totalCount;
        private Long activeCount;
        private Long withdrawnCount;
    }
}
