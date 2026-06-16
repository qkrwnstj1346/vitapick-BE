package com.vita.vitapickBack.admin;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vita.vitapickBack.order.Ord;

@Repository
public interface AdminOrdRepository extends JpaRepository<Ord, Long> {

    @Query("""
            SELECT DISTINCT o
            FROM Ord o
            WHERE (:keyword IS NULL OR :keyword = ''
                   OR LOWER(o.ordNo) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR EXISTS (
                       SELECT 1
                       FROM Users u
                       WHERE u.userNum = o.userNum
                         AND (LOWER(u.loginId) LIKE LOWER(CONCAT('%', :keyword, '%'))
                              OR LOWER(u.userNm) LIKE LOWER(CONCAT('%', :keyword, '%')))
                   )
                   OR EXISTS (
                       SELECT 1
                       FROM OrdIt oi
                       WHERE oi.ordId = o.ordId
                         AND LOWER(oi.prdNm) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   ))
              AND o.ordStCd = 'PAID'
              AND (:categoryId IS NULL OR EXISTS (
                  SELECT 1
                  FROM OrdIt oi, Prd p
                  WHERE oi.ordId = o.ordId
                    AND p.prdId = oi.prdId
                    AND p.catCd = :categoryId
              ))
              AND (:startAt IS NULL OR o.crtAt >= :startAt)
              AND (:endAt IS NULL OR o.crtAt < :endAt)
            """)
    Page<Ord> findAdminOrders(
            @Param("keyword") String keyword,
            @Param("categoryId") Integer categoryId,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt,
            Pageable pageable);
}
