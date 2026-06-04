package com.vita.vitapickBack.products.rvw;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface RvwRepository extends JpaRepository<Rvw, Long> {
    
	// 상품 ID로 리뷰 조회 (최신순)
	List<Rvw> findByPrdIdOrderByCrtAtDesc(Long prdId);
	
	// 회원 ID로 리뷰 조회 (최신순)
	List<Rvw> findByUserNumOrderByCrtAtDesc(Long userNum);
	
	// 상품 ID, 사용 여부로 리뷰 조회 (최신순)
	List<Rvw> findByPrdIdAndUseYnOrderByCrtAtDesc(Long prdId, String useYn);
}