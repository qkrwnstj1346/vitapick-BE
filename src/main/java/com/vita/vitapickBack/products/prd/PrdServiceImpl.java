package com.vita.vitapickBack.products.prd;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.vita.vitapickBack.products.prd_img.PrdImg;
import com.vita.vitapickBack.products.prd_img.PrdImgRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrdServiceImpl implements PrdService {

    // 상품 DB 접근하는 애
    private final PrdRepository prdRepository;

    // 이미지 DB 접근하는 애
    private final PrdImgRepository prdImgRepository;

    // 카테고리별 상품 목록 + 썸네일 이미지 반환
    @Override
    public List<PrdDTO> getPrdByCategory(int catCd) {
        List<Prd> prdList = prdRepository.findByCatCd(catCd);
        List<PrdDTO> result = new ArrayList<>();
        for (Prd prd : prdList) {
            String thumbUrl = null;
            List<PrdImg> imgList = prdImgRepository.findByPrdId(prd.getPrdId());
            for (PrdImg img : imgList) {
                if ("THUMB".equals(img.getImgTypeCd())) {
                    thumbUrl = img.getImgUrl();
                    break;
                }
            }
            PrdDTO dto = PrdDTO.builder()
                    .prdId(prd.getPrdId())
                    .prdNm(prd.getPrdNm())
                    .price(prd.getPrice())
                    .brand(prd.getBrand())
                    .descTxt(prd.getDescTxt())
                    .ingr(prd.getIngr())
                    .thumbImgUrl(thumbUrl)
                    .build();
            result.add(dto);
        }
        return result;
    }

    // 상품 상세 조회
    @Override
    public PrdDTO getPrdDetail(Long prdId) {
        Optional<Prd> result = prdRepository.findById(prdId);
        if (!result.isPresent()) {
            throw new RuntimeException("상품을 찾을 수 없습니다.");
        }
        Prd prd = result.get();
        String thumbUrl = null;
        List<PrdImg> imgList = prdImgRepository.findByPrdId(prdId);
        for (PrdImg img : imgList) {
            if ("THUMB".equals(img.getImgTypeCd())) {
                thumbUrl = img.getImgUrl();
                break;
            }
        }
        return PrdDTO.builder()
                .prdId(prd.getPrdId())
                .prdNm(prd.getPrdNm())
                .price(prd.getPrice())
                .brand(prd.getBrand())
                .descTxt(prd.getDescTxt())
                .ingr(prd.getIngr())
                .thumbImgUrl(thumbUrl)
                .build();
    }
}