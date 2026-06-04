package com.vita.vitapickBack.products.rvw;

import java.util.List;

public interface RvwService {

    // 리뷰 작성
    Rvw createRvw(Long userNum, RvwDTO dto);

    // 상품 ID로 리뷰 조회
    List<Rvw> findByPrdId(Long prdId);
    
    // 회원 ID로 리뷰 조회
    List<Rvw> findByUserNum(Long userNum);
    
    // 리뷰 단건 조회
    Rvw findByRvwId(Long rvwId);
    
    // 리뷰 취소
    void cancelRvw(Long userNum, Long rvwId);

}