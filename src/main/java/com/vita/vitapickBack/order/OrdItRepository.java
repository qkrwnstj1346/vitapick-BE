package com.vita.vitapickBack.order;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vita.vitapickBack.products.prd_img.PrdImg;

@Repository
public interface OrdItRepository extends JpaRepository<OrdIt, Long> {

    // 주문별 주문상품 목록 조회
    List<OrdIt> findByOrdId(Long ordId);

    // 상품별 주문 조회
    List<OrdIt> findByPrdId(Long prdId);

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