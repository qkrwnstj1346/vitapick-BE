package com.vita.vitapickBack.products.rvw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RvwDTO {

    // 주문 상품 ID
    private Long ordItId;

    // 회원 ID
    private Long userNum;
    
    // 상품 ID
    private Long prdId;

    // 평점
    private Integer rating;

    // 리뷰 내용
    private String cmt;

}