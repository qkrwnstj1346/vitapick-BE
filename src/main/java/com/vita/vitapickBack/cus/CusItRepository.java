package com.vita.vitapickBack.cus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CusItRepository extends JpaRepository<CusIt, Long> {

    // cusId로 추천 상품 목록 조회 (sort_num 오름차순)
    List<CusIt> findByCustom_CusIdOrderBySortNumAsc(Long cusId);
 
    // cusId 목록으로 추천 상품 일괄 조회 (이력 페이지 N+1 방지)
    @Query("SELECT ci FROM CustIt ci " +
           "JOIN FETCH ci.prd " +
           "WHERE ci.cus.cusId IN :cusIds " +
           "ORDER BY ci.cus.cusId, ci.sortNum ASC")
    List<CusIt> findByCusIds(@Param("cusIds") List<Long> cusIds);
}
