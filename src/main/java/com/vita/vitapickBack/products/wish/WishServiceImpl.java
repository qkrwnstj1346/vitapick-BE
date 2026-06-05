package com.vita.vitapickBack.products.wish;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

// 찜 Service 구현체
@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {

    // 찜 Repository
    private final WishRepository wishRepository;

    // 찜 추가
    @Override
    public Wish addWish(Long userNum, WishDTO dto) {

        // 이미 찜한 상품이면 중복 등록 막기
        boolean exists = wishRepository.existsByUserNumAndPrdId(userNum, dto.getPrdId());

        if (exists) {
            throw new RuntimeException("이미 찜한 상품입니다.");
        }

        // Wish 엔티티 생성
        Wish wish = Wish.builder()
                .userNum(userNum)
                .prdId(dto.getPrdId())
                .crtAt(LocalDateTime.now())
                .build();

        // DB 저장
        return wishRepository.save(wish);
    }

    // 찜 취소
    @Override
    @Transactional
    public void deleteWish(Long userNum, Long prdId) {

        // 해당 회원의 해당 상품 찜 삭제
        wishRepository.deleteByUserNumAndPrdId(userNum, prdId);
    }

    // 찜 여부 확인
    @Override
    public boolean isWished(Long userNum, Long prdId) {

        // 찜이 있으면 true, 없으면 false
        return wishRepository.existsByUserNumAndPrdId(userNum, prdId);
    }

    // 내 찜 목록 조회
    @Override
    public List<Wish> findByUserNum(Long userNum) {

        // 로그인한 회원의 찜 목록 최신순 조회
        return wishRepository.findByUserNumOrderByCrtAtDesc(userNum);
    }

    // 찜 토글
    // 이미 찜했으면 삭제하고 false 반환
    // 찜 안 했으면 추가하고 true 반환
    @Override
    @Transactional
    public boolean toggleWish(Long userNum, Long prdId) {

        boolean exists = wishRepository.existsByUserNumAndPrdId(userNum, prdId);

        if (exists) {
            wishRepository.deleteByUserNumAndPrdId(userNum, prdId);
            return false;
        }

        Wish wish = Wish.builder()
                .userNum(userNum)
                .prdId(prdId)
                .crtAt(LocalDateTime.now())
                .build();

        wishRepository.save(wish);
        return true;
    }
}