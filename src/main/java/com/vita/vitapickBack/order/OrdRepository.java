package com.vita.vitapickBack.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdRepository extends JpaRepository<Ord, Long> {

    // 회원 주문 목록 조회
    List<Ord> findByUserNum(Long userNum);

    // 주문번호 조회
    Ord findByOrdNo(String ordNo);

    // 주문 상세 조회
    // 기본 제공 메서드 사용
    // findById(ordId)

    // 주문 생성
    // 기본 제공 메서드 사용
    // save(ord)

}