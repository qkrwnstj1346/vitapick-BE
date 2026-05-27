package com.vita.vitapickBack.products.rvw;

import java.util.List;

public interface RvwService {

    // 리뷰 작성
    Rvw createRvw(RvwDTO dto);

    // 상품 ID로 리뷰 조회
    List<Rvw> findByPrdId(Long prdId);

}