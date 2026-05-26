package com.vita.vitapickBack.products.prd;

import java.util.List;

public interface PrdService {
	
   
    // 카테고리별 상품 조회 
    List<PrdDTO> getPrdByCategory(int catCd);
    
    // 상품 상세 조회
    PrdDTO getPrdDetail(Long prdId);
    
    // 상품 검색
    List<PrdDTO> searchPrd(String keyword);
}
