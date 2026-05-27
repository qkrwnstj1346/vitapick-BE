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
    public Rvw createRvw(RvwDTO dto) {
        Rvw rvw = Rvw.builder()
                .userNum(dto.getUserNum())
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

}