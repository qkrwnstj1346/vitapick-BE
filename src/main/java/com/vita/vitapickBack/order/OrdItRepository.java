package com.vita.vitapickBack.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdItRepository extends JpaRepository<OrdIt, Long> {

	// 주문별 주문상품 목록 조회
	List<OrdIt> findByOrdId(Long ordId);

	// 상품별 주문 조회
	List<OrdIt> findByPrdId(Long prdId);

	// 상품별 판매량 TOP5 집계
	@Query(value = """
						SELECT
			    oi.prd_id,
			    oi.prd_nm,
			    pi.img_url,
			    SUM(oi.it_qty) total_qty
			FROM ord_it oi
			JOIN prd_img pi
			    ON oi.prd_id = pi.prd_id
			WHERE pi.img_type_cd = 'THUMB'
			GROUP BY oi.prd_id, oi.prd_nm, pi.img_url
			ORDER BY total_qty DESC
			LIMIT 5
						""", nativeQuery = true)
	List<Object[]> findTopProducts();

	// 주문상품 상세 조회
	// 기본 제공 메서드 사용
	// findById(ordItId)

	// 주문상품 저장
	// 기본 제공 메서드 사용
	// save(ordIt)

	// 주문상품 삭제
	// 기본 제공 메서드 사용
	// deleteById(ordItId)

}