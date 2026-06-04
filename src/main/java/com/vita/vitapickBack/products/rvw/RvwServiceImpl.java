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
    
    // 상품 ID로 리뷰 조회 (최신순)
    @Override
    public List<Rvw> findByPrdId(Long prdId) {
        return rvwRepository.findByPrdIdOrderByCrtAtDesc(prdId);
    }
    
    // 회원 ID로 리뷰 조회
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
    
    // 리뷰 삭제 (useYn = 'N'으로 변경)
    @Override
    public void cancelRvw(Long userNum, Long rvwId) {
        Rvw rvw = rvwRepository.findById(rvwId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        if (!rvw.getUserNum().equals(userNum)) {
            throw new RuntimeException("본인의 리뷰만 취소할 수 있습니다.");
        }

        rvw.setUseYn("N");
        rvw.setUpdAt(LocalDateTime.now());
        rvwRepository.save(rvw);
    }

}