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
        // DB에서 catCd로 상품 조회
        List<Prd> prdList = prdRepository.findByCatCd(catCd);
        List<PrdDTO> result = new ArrayList<>();

        for (Prd prd : prdList) {
            String thumbUrl = null;
            List<PrdImg> imgList = prdImgRepository.findByPrdId(prd.getPrdId());
            
            // 썸네일 이미지 찾기
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

        // 이미지 목록 가져오기
        List<PrdImg> imgList = prdImgRepository.findByPrdId(prdId);

        // 썸네일이랑 상세 이미지 둘 다 찾기
        String thumbUrl = null;
        String detailUrl = null;
        for (PrdImg img : imgList) {
            // 썸네일 이미지
            if ("THUMB".equals(img.getImgTypeCd())) {
                thumbUrl = img.getImgUrl();
            }
            // 상세 이미지
            if ("DETAIL".equals(img.getImgTypeCd())) {
                detailUrl = img.getImgUrl();
            }
        }

        // 찾은 정보 담아서 반환
        return PrdDTO.builder()
                .prdId(prd.getPrdId())
                .prdNm(prd.getPrdNm())
                .price(prd.getPrice())
                .brand(prd.getBrand())
                .descTxt(prd.getDescTxt())
                .ingr(prd.getIngr())
                .thumbImgUrl(thumbUrl)
                .detailImgUrl(detailUrl)
                .build();
    }
    // 상품 검색
    @Override
    	public List<PrdDTO> searchPrd(String keyword) {
        // 검색어가 포함된 상품 목록 가져오기
    	List<Prd> prdList = prdRepository.searchByKeyword(keyword);
    	
        List<PrdDTO> result = new ArrayList<>(); 
        // 각 상품마다 썸네일 이미지 찾아서 DTO에 담기
        for (Prd prd : prdList) {
            String thumbUrl = null;
            List<PrdImg> imgList = prdImgRepository.findByPrdId(prd.getPrdId());
            // 썸네일 이미지 찾기
            for (PrdImg img : imgList) {
                if ("THUMB".equals(img.getImgTypeCd())) {
                    thumbUrl = img.getImgUrl();
                    break; 
                }
            }
            // Builder 패턴으로 DTO 생성
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
}