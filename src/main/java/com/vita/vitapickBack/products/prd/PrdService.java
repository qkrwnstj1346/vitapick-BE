package com.vita.vitapickBack.products.prd;

import java.util.List;

public interface PrdService {
	
	// 상품 전체 목록 조회
    List<Prd> getAllPrd();
    
    // 상품 + 이미지 같이 반환
    List<PrdDTO> getAllPrdWithImg();
}
