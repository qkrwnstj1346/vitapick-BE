package com.vita.vitapickBack.products.prd;

import java.util.List;

public interface PrdService {
	
	// 상품 전체 목록 조회
    List<Prd> getAllPrd();
    
    // 상품 + 이미지 같이 반환
    List<PrdDTO> getAllPrdWithImg();
    
    // 카테고리별 상품 조회 
    List<PrdDTO> getPrdByCategory(int catCd);
    
    // 상품 상세 조회
    PrdDTO getPrdDetail(Long prdId);
}
