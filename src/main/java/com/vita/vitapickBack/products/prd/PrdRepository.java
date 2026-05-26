package com.vita.vitapickBack.products.prd;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PrdRepository extends JpaRepository<Prd, Long> {
	
	// 카테고리별 상품 조회
	List<Prd> findByCatCd(int catCd);
	// useYn 에 따라 조회
	List<Prd> findByUseYn(String useYn);
}