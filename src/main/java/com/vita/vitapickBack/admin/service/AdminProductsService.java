package com.vita.vitapickBack.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vita.vitapickBack.admin.dto.AdminProductsResponseDTO;
import com.vita.vitapickBack.admin.dto.AdminProductsResponseDTO.AdminProductDTO;
import com.vita.vitapickBack.admin.repository.AdminPrdRepository;
import com.vita.vitapickBack.products.prd.Prd;
import com.vita.vitapickBack.products.prd_img.PrdImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminProductsService {

    private static final String THUMB = "THUMB";

    private final AdminPrdRepository adminPrdRepository;
    private final PrdImgRepository prdImgRepository;

    // 관리자 상품 목록 조회 조건을 처리한다.
    public AdminProductsResponseDTO getProducts(
            int page,
            int size,
            String keyword,
            String status,
            Integer categoryId) {

        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 10 : Math.min(size, 100);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "crtAt"));

        Page<Prd> productsPage = adminPrdRepository.findAdminProducts(keyword, status, categoryId, pageable);

        return AdminProductsResponseDTO.builder()
                .content(productsPage.getContent().stream()
                        .map(this::toAdminProductDTO)
                        .toList())
                .page(productsPage.getNumber())
                .size(productsPage.getSize())
                .totalElements(productsPage.getTotalElements())
                .totalPages(productsPage.getTotalPages())
                .build();
    }

    // 관리자 상품 응답 DTO로 변환한다.
    private AdminProductDTO toAdminProductDTO(Prd prd) {
        String thumbImgUrl = prdImgRepository.findByPrdIdAndImgTypeCd(prd.getPrdId(), THUMB)
                .map(img -> img.getImgUrl())
                .orElse(null);

        return AdminProductDTO.builder()
                .prdId(prd.getPrdId())
                .prdNm(prd.getPrdNm())
                .brand(prd.getBrand())
                .catCd(prd.getCatCd())
                .categoryName(getCategoryName(prd.getCatCd()))
                .price(prd.getPrice())
                .useYn(prd.getUseYn())
                .thumbImgUrl(thumbImgUrl)
                .crtAt(prd.getCrtAt())
                .updAt(prd.getUpdAt())
                .wdAt(prd.getWdAt())
                .build();
    }

    // 관리자 상품 카테고리명을 조회한다.
    private String getCategoryName(Integer catCd) {
        if (catCd == null) {
            return null;
        }
        return "Category " + catCd;
    }
}
