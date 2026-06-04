package com.vita.vitapickBack.products.rvw;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RvwServiceImpl implements RvwService {

    // 리뷰 DB 작업을 담당하는 Repository
    private final RvwRepository rvwRepository;

    // 리뷰 작성
    @Override
    public Rvw createRvw(Long userNum, RvwDTO dto) {

        // 프론트에서 받은 리뷰 내용을 Rvw 엔티티로 변환
        Rvw rvw = Rvw.builder()
                .userNum(userNum)              // 로그인한 회원 번호
                .ordItId(dto.getOrdItId())     // 주문 상품 ID
                .prdId(dto.getPrdId())         // 상품 ID
                .rating(dto.getRating())       // 별점
                .cmt(dto.getCmt())             // 리뷰 내용
                .useYn("Y")                    // 사용 여부: Y = 정상 리뷰
                .crtAt(LocalDateTime.now())    // 작성 시간
                .build();

        // 리뷰 저장
        return rvwRepository.save(rvw);
    }

    // 상품 ID로 리뷰 조회
    @Override
    public List<Rvw> findByPrdId(Long prdId) {

        // 해당 상품의 리뷰 중 useYn이 Y인 리뷰만 최신순으로 조회
        return rvwRepository.findByPrdIdAndUseYnOrderByCrtAtDesc(prdId, "Y");
    }

    // 회원 번호로 리뷰 조회
    @Override
    public List<Rvw> findByUserNum(Long userNum) {

        // 해당 회원이 작성한 리뷰를 최신순으로 조회
        // 현재는 useYn = N인 삭제 처리 리뷰도 포함될 수 있음
        return rvwRepository.findByUserNumOrderByCrtAtDesc(userNum);
    }

    // 리뷰 단건 조회
    @Override
    public Rvw findByRvwId(Long rvwId) {

        // rvwId로 리뷰를 찾고, 없으면 예외 발생
        return rvwRepository.findById(rvwId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
    }

    // 리뷰 삭제
    // 실제 DB에서 delete 하는 것이 아니라 useYn을 N으로 바꾸는 소프트 삭제 방식
    @Override
    public void cancelRvw(Long userNum, Long rvwId) {

        // 삭제할 리뷰 조회
        Rvw rvw = rvwRepository.findById(rvwId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        // 본인이 작성한 리뷰인지 확인
        if (!rvw.getUserNum().equals(userNum)) {
            throw new RuntimeException("본인의 리뷰만 취소할 수 있습니다.");
        }

        // 삭제 처리: useYn을 N으로 변경
        rvw.setUseYn("N");

        // 수정 시간 저장
        rvw.setUpdAt(LocalDateTime.now());

        // 변경 내용 저장
        rvwRepository.save(rvw);
    }
    
    // 리뷰 수정
    @Override
    public Rvw updateRvw(Long userNum, Long rvwId, RvwDTO dto) {

        // 수정할 리뷰 조회
        Rvw rvw = rvwRepository.findById(rvwId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        // 본인이 작성한 리뷰인지 확인
        if (!rvw.getUserNum().equals(userNum)) {
            throw new RuntimeException("본인의 리뷰만 수정할 수 있습니다.");
        }

        // 삭제된 리뷰는 수정 못 하게 막기
        if (!"Y".equals(rvw.getUseYn())) {
            throw new RuntimeException("삭제된 리뷰는 수정할 수 없습니다.");
        }

        // 별점 수정
        rvw.setRating(dto.getRating());

        // 리뷰 내용 수정
        rvw.setCmt(dto.getCmt());

        // 수정 시간 저장
        rvw.setUpdAt(LocalDateTime.now());

        // DB 저장
        return rvwRepository.save(rvw);
    }
}