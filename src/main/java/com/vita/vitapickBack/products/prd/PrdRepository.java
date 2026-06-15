package com.vita.vitapickBack.products.prd;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PrdRepository extends JpaRepository<Prd, Long> {
	
	// 카테고리별 상품 조회
	List<Prd> findByCatCd(int catCd);
	
	// useYn 에 따라 조회
	List<Prd> findByUseYn(String useYn);
	
	// 상품명으로 검색
	@Query("SELECT p FROM Prd p WHERE p.prdNm LIKE %:keyword%")
	List<Prd> searchByKeyword(@Param("keyword") String keyword);

	@Query("""
			SELECT p
			FROM Prd p
			WHERE (:keyword IS NULL OR :keyword = ''
				   OR LOWER(p.prdNm) LIKE LOWER(CONCAT('%', :keyword, '%')))
			  AND (:status IS NULL OR :status = '' OR p.useYn = :status)
			  AND (:categoryId IS NULL OR p.catCd = :categoryId)
			""")
	Page<Prd> findAdminProducts(
			@Param("keyword") String keyword,
			@Param("status") String status,
			@Param("categoryId") Integer categoryId,
			Pageable pageable);
}
