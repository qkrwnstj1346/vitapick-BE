package com.vita.vitapickBack.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vita.vitapickBack.products.prd.Prd;

@Repository
public interface AdminPrdRepository extends JpaRepository<Prd, Long> {

    @Query("""
            SELECT p
            FROM Prd p
            WHERE (:keyword IS NULL OR :keyword = ''
                   OR LOWER(p.prdNm) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:status IS NULL OR :status = '' OR p.useYn = :status)
              AND (:categoryId IS NULL OR p.catCd = :categoryId)
            """)
    Page<Prd> findAdminProducts(
            @Param("keyword") String keyword,
            @Param("status") String status,
            @Param("categoryId") Integer categoryId,
            Pageable pageable);
}
