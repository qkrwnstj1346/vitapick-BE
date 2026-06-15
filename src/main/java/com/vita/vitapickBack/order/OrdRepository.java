package com.vita.vitapickBack.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdRepository extends JpaRepository<Ord, Long> {

    // 회원 주문 목록 조회
    List<Ord> findByUserNumOrderByOrdIdDesc(Long userNum);

    // 주문번호 조회
    Ord findByOrdNo(String ordNo);

    @Query("""
            SELECT COALESCE(SUM(o.totalAmt), 0)
            FROM Ord o
            WHERE o.ordStCd = :ordStCd
              AND o.crtAt >= :startAt
              AND o.crtAt < :endAt
            """)
    Long sumTotalAmtByOrdStCdAndCrtAtRange(
            @Param("ordStCd") String ordStCd,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt);

    Long countByOrdStCdAndCrtAtGreaterThanEqualAndCrtAtLessThan(
            String ordStCd,
            LocalDateTime startAt,
            LocalDateTime endAt);

    @Query("""
            SELECT o
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
              AND (:status IS NULL OR :status = '' OR o.ordStCd = :status)
              AND (:startAt IS NULL OR o.crtAt >= :startAt)
              AND (:endAt IS NULL OR o.crtAt < :endAt)
            """)
    Page<Ord> findAdminOrders(
            @Param("keyword") String keyword,
            @Param("status") String status,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt,
            Pageable pageable);

    // 주문 상세 조회
    // 기본 제공 메서드 사용
    // findById(ordId)

    // 주문 생성
    // 기본 제공 메서드 사용
    // save(ord)

}
