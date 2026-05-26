package com.vita.vitapickBack.useraddr;

import java.util.List;

public interface UserAddrService {

    // 회원 배송지 목록 조회
    List<UserAddr> findByUserNum(Long userNum);

    // 배송지 등록
    UserAddr createAddr(UserAddrDTO dto);

    // 배송지 수정
    UserAddr updateAddr(Long addrId, UserAddrDTO dto);

    // 배송지 삭제
    void deleteAddr(Long addrId);

    // 기본 배송지 변경
    void updateBaseAddr(Long userNum, Long addrId);

}