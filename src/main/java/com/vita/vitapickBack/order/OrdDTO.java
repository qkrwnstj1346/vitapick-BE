package com.vita.vitapickBack.order;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class OrdDTO {

    private Long ordId;
    private Long userNum;
    private String ordNo;
    private Long addrId;
    private String ordStCd;
    private Integer totalAmt;
    private LocalDateTime crtAt;
    private LocalDateTime updAt;
    
    // 주문 상품 목록 
    private List<OrdItDTO> prdList;
    
    // 결제 정보
    private PayDTO paydto;
}