package com.vita.vitapickBack.products.rvw;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RvwRepository extends JpaRepository<Rvw, Long> {

    // 회원 ID로 리뷰 조회 (최신순)
    List<Rvw> findByUserNumOrderByCrtAtDesc(Long userNum);

    // 상품 ID로 리뷰 조회 (최신순)
    List<Rvw> findByPrdIdOrderByCrtAtDesc(Long prdId);

    @Query("""
            SELECT r
            FROM Rvw r
            WHERE (:keyword IS NULL OR :keyword = ''
                   OR LOWER(r.cmt) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR EXISTS (
                       SELECT 1
                       FROM Users u
                       WHERE u.userNum = r.userNum
                         AND (LOWER(u.loginId) LIKE LOWER(CONCAT('%', :keyword, '%'))
                              OR LOWER(u.userNm) LIKE LOWER(CONCAT('%', :keyword, '%')))
                   )
                   OR EXISTS (
                       SELECT 1
                       FROM Prd p
                       WHERE p.prdId = r.prdId
                         AND LOWER(p.prdNm) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   ))
              AND (:rating IS NULL OR r.rating = :rating)
              AND (:startAt IS NULL OR r.crtAt >= :startAt)
              AND (:endAt IS NULL OR r.crtAt < :endAt)
            """)
    Page<Rvw> findAdminReviews(
            @Param("keyword") String keyword,
            @Param("rating") Integer rating,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt,
            Pageable pageable);
}
