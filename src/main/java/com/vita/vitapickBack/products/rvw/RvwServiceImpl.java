package com.vita.vitapickBack.products.rvw;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RvwServiceImpl implements RvwService {

    private final RvwRepository rvwRepository;

    // 리뷰 작성
    @Override
    public Rvw createRvw(Long userNum, RvwDTO dto) {

        Rvw rvw = Rvw.builder()
                .userNum(userNum)
                .ordItId(dto.getOrdItId())
                .prdId(dto.getPrdId())
                .rating(dto.getRating())
                .cmt(dto.getCmt())
                .useYn("Y")
                .crtAt(LocalDateTime.now())
                .build();

        return rvwRepository.save(rvw);
    }

    // 상품 ID로 리뷰 조회
    @Override
    public List<Rvw> findByPrdId(Long prdId) {

        return rvwRepository.findByPrdIdOrderByCrtAtDesc(prdId);
    }

    // 회원 번호로 리뷰 조회
    @Override
    public List<Rvw> findByUserNum(Long userNum) {

        return rvwRepository.findByUserNumOrderByCrtAtDesc(userNum);
    }

    // 리뷰 단건 조회
    @Override
    public Rvw findByRvwId(Long rvwId) {

        return rvwRepository.findById(rvwId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
    }

    // 리뷰 삭제
    @Override
    public void deleteRvw(Long userNum, Long rvwId) {

        Rvw rvw = rvwRepository.findById(rvwId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        if (!rvw.getUserNum().equals(userNum)) {
            throw new RuntimeException("본인의 리뷰만 삭제할 수 있습니다.");
        }

        rvwRepository.delete(rvw);
    }

    // 리뷰 수정
    @Override
    public Rvw updateRvw(Long userNum, Long rvwId, RvwDTO dto) {

        Rvw rvw = rvwRepository.findById(rvwId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        if (!rvw.getUserNum().equals(userNum)) {
            throw new RuntimeException("본인의 리뷰만 수정할 수 있습니다.");
        }

        rvw.setRating(dto.getRating());
        rvw.setCmt(dto.getCmt());
        rvw.setUpdAt(LocalDateTime.now());

        return rvwRepository.save(rvw);
    }

    // 관리자 리뷰 답글 등록
    @Override
    public Rvw createRvwReply(Long rvwId, String replyTxt) {

        if (replyTxt == null || replyTxt.isBlank()) {
            throw new RuntimeException("리뷰 답글 내용을 입력해주세요.");
        }

        Rvw rvw = rvwRepository.findById(rvwId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        if (rvw.getReplyTxt() != null && !rvw.getReplyTxt().isBlank()) {
            throw new RuntimeException("이미 등록된 답글이 있습니다.");
        }

        rvw.setReplyTxt(replyTxt);
        rvw.setReplyAt(LocalDateTime.now());
        rvw.setUpdAt(LocalDateTime.now());

        return rvwRepository.save(rvw);
    }

    // 관리자 리뷰 답글 수정
    @Override
    public Rvw updateRvwReply(Long rvwId, String replyTxt) {

        if (replyTxt == null || replyTxt.isBlank()) {
            throw new RuntimeException("리뷰 답글 내용을 입력해주세요.");
        }

        Rvw rvw = rvwRepository.findById(rvwId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        if (rvw.getReplyTxt() == null || rvw.getReplyTxt().isBlank()) {
            throw new RuntimeException("수정할 답글이 없습니다.");
        }

        rvw.setReplyTxt(replyTxt);
        rvw.setReplyAt(LocalDateTime.now());
        rvw.setUpdAt(LocalDateTime.now());

        return rvwRepository.save(rvw);
    }

    // 관리자 리뷰 답글 삭제
    @Override
    public Rvw deleteRvwReply(Long rvwId) {

        Rvw rvw = rvwRepository.findById(rvwId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        if (rvw.getReplyTxt() == null || rvw.getReplyTxt().isBlank()) {
            throw new RuntimeException("삭제할 답글이 없습니다.");
        }

        rvw.setReplyTxt(null);
        rvw.setReplyAt(null);
        rvw.setUpdAt(LocalDateTime.now());

        return rvwRepository.save(rvw);
    }
}