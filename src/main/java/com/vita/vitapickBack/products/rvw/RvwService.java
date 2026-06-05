package com.vita.vitapickBack.products.rvw;

import java.util.List;

public interface RvwService {

    // 리뷰 작성
    Rvw createRvw(Long userNum, RvwDTO dto);

    // 상품 ID로 리뷰 목록 조회
    List<Rvw> findByPrdId(Long prdId);

    // 로그인한 사용자의 리뷰 목록 조회
    List<Rvw> findByUserNum(Long userNum);

    // 리뷰 단건 조회
    Rvw findByRvwId(Long rvwId);

    // 리뷰 삭제
    void deleteRvw(Long userNum, Long rvwId);

    // 리뷰 수정
    Rvw updateRvw(Long userNum, Long rvwId, RvwDTO dto);

    // 관리자 리뷰 답글 등록
    Rvw createRvwReply(Long rvwId, String replyTxt);

    // 관리자 리뷰 답글 수정
    Rvw updateRvwReply(Long rvwId, String replyTxt);

    // 관리자 리뷰 답글 삭제
    Rvw deleteRvwReply(Long rvwId);
}