package com.vita.vitapickBack.products.rvw;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RvwRepository extends JpaRepository<Rvw, Long> {
    
    // 상품 ID로 리뷰 조회
    List<Rvw> findByPrdId(Long prdId);

}