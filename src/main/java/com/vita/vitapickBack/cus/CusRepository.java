package com.vita.vitapickBack.cus;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CusRepository extends JpaRepository<Cus, Long> {

	 // 특정 회원의 전체 추천 이력 (최신순)
    List<Cus> findByUsers_UserNumOrderByCrtAtDesc(Long userNum);
 
    // cusId + userNum 으로 단건 조회 (본인 소유 확인)
    Optional<Cus> findByCusIdAndUsers_UserNum(Long cusId, Long userNum);
 
    // 가장 최근 커스텀 1건
    @Query("SELECT c FROM Cus c WHERE c.users.userNum = :userNum ORDER BY c.crtAt DESC LIMIT 1")
    Optional<Cus> findLatestByUserNum(@Param("userNum") Long userNum);
}
