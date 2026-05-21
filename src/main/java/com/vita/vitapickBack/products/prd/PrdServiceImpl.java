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
        return getPrdDTOList(prdRepository.findAll());
    }

    @Override
    public List<PrdDTO> getPrdByCategory(int catCd) {
        return getPrdDTOList(prdRepository.findByCatCd(catCd));
    }

    private List<PrdDTO> getPrdDTOList(List<Prd> prdList) {
        List<PrdDTO> result = new ArrayList<>();
        for (Prd prd : prdList) {
            List<PrdImg> imgList = prdImgRepository.findByPrdId(prd.getPrdId());
            String thumbUrl = null;
            for (PrdImg img : imgList) {
                if ("THUMB".equals(img.getImgTypeCd())) {
                    thumbUrl = img.getImgUrl();
                    break;
                }
            }
            result.add(new PrdDTO(prd.getPrdId(), prd.getPrdNm(), prd.getPrice(), prd.getBrand(), prd.getDescTxt(), prd.getIngr(), thumbUrl));
        }
        return result;
    }
}