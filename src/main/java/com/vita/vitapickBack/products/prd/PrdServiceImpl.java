package com.vita.vitapickBack.products.prd;

import java.util.ArrayList;
import java.util.List;
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

    // 상품 전체 목록 조회
    @Override
    public List<Prd> getAllPrd() {
        return prdRepository.findAll();
    }

    // 상품 + 썸네일 이미지 같이 반환
    @Override
    public List<PrdDTO> getAllPrdWithImg() {

        // 상품 전체 가져오기
        List<Prd> prdList = prdRepository.findAll();
        List<PrdDTO> result = new ArrayList<>();

        for (Prd prd : prdList) {
            // 이 상품의 이미지 목록 가져오기
            List<PrdImg> imgList = prdImgRepository.findByPrdId(prd.getPrdId());

            // 썸네일 이미지 URL 찾기
            String thumbUrl = null;
            for (PrdImg img : imgList) {
                if ("THUMB".equals(img.getImgTypeCd())) {
                    thumbUrl = img.getImgUrl();
                    break;
                }
            }

            // 상품 정보 + 이미지 URL 담기
            PrdDTO dto = new PrdDTO(
                    prd.getPrdId(),
                    prd.getPrdNm(),
                    prd.getPrice(),
                    prd.getBrand(),
                    prd.getDescTxt(),
                    prd.getIngr(),
                    thumbUrl
            );
            result.add(dto);
        }
        return result;
    }
}