package com.vita.vitapickBack.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdItRepository extends JpaRepository<OrdIt, Long> {

	List<OrdIt> findByOrdId(Long ordId);

	List<OrdIt> findByPrdId(Long prdId);
	
	// 상품별 판매량 기준 상위 5개 상품 ID, 상품명, 썸네일 이미지 URL, 판매량 조회
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
	
	// 월별 카테고리별 판매금액과 판매량 조회
	@Query(value = """
			SELECT
			    p.cat_cd,
			    COALESCE(SUM(oi.it_amt), 0) AS sales_amt,
			    COALESCE(SUM(oi.it_qty), 0) AS total_qty
			FROM ord o
			JOIN ord_it oi
			    ON o.ord_id = oi.ord_id
			JOIN prd p
			    ON oi.prd_id = p.prd_id
			WHERE o.ord_st_cd = 'PAID'
			    AND o.crt_at >= :startAt
			    AND o.crt_at < :endAt
			GROUP BY p.cat_cd
			ORDER BY sales_amt DESC
			LIMIT 1
			""", nativeQuery = true)
	List<Object[]> findPopularCategorySales(
			@Param("startAt") java.time.LocalDateTime startAt,
			@Param("endAt") java.time.LocalDateTime endAt);
	
	// 상품별 판매량 기준 상위 5개 상품 ID, 상품명, 카테고리 코드, 판매량, 판매금액 조회
	@Query(value = """
			SELECT
			    oi.prd_id,
			    COALESCE(oi.prd_nm, p.prd_nm) AS prd_nm,
			    p.cat_cd,
			    COALESCE(SUM(oi.it_qty), 0) AS paid_qty,
			    COALESCE(SUM(oi.it_amt), 0) AS sales_amt
			FROM ord o
			JOIN ord_it oi
			    ON o.ord_id = oi.ord_id
			JOIN prd p
			    ON oi.prd_id = p.prd_id
			WHERE o.ord_st_cd = 'PAID'
			GROUP BY oi.prd_id, COALESCE(oi.prd_nm, p.prd_nm), p.cat_cd
			ORDER BY sales_amt DESC, paid_qty DESC, oi.prd_id ASC
			LIMIT 5
			""", nativeQuery = true)
	List<Object[]> findProductSalesTop5();
	
	// 월별 상품 판매량 기준 상위 5개 상품 조회
	@Query(value = """
			SELECT
			    oi.prd_id,
			    COALESCE(oi.prd_nm, p.prd_nm) AS prd_nm,
			    p.cat_cd,
			    COALESCE(SUM(oi.it_qty), 0) AS paid_qty,
			    COALESCE(SUM(oi.it_amt), 0) AS sales_amt
			FROM ord o
			JOIN ord_it oi
			    ON o.ord_id = oi.ord_id
			JOIN prd p
			    ON oi.prd_id = p.prd_id
			WHERE o.ord_st_cd = 'PAID'
			    AND o.crt_at >= :startAt
			    AND o.crt_at < :endAt
			GROUP BY oi.prd_id, COALESCE(oi.prd_nm, p.prd_nm), p.cat_cd
			ORDER BY sales_amt DESC, paid_qty DESC, oi.prd_id ASC
			LIMIT 5
			""", nativeQuery = true)
	List<Object[]> findMonthlyProductSalesTop5(
			@Param("startAt") java.time.LocalDateTime startAt,
			@Param("endAt") java.time.LocalDateTime endAt);
	
	// 상품별 판매량 기준 상위 10개 상품 ID 조회
	@Query(value = """
	        SELECT
	            oi.prd_id
	        FROM ord o
	        JOIN ord_it oi
	            ON o.ord_id = oi.ord_id
	        WHERE o.ord_st_cd = 'PAID'
	        GROUP BY oi.prd_id
	        ORDER BY SUM(oi.it_qty) DESC, oi.prd_id ASC
	        LIMIT 10
	        """, nativeQuery = true)
	List<Long> findBestProductIdsTop10();

}
