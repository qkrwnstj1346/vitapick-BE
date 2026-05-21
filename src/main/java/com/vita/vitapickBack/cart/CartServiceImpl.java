package com.vita.vitapickBack.cart;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	
	private final CartRepository cartRepository;

	// 회원 장바구니 목록 조회
	@Override
	public List<Cart> findByUserNum(Long userNum) {
		return cartRepository.findByUserNum(userNum);
	}

	// 동일 상품 체크
	@Override
	public Cart findByUserNumAndPrdId(Long userNum, Long prdId) {
		return cartRepository.findByUserNumAndPrdId(userNum, prdId);
	}

	// 장바구니 담기
	@Override
	public Cart addCart(CartDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}


	// 장바구니 수량 증가/감소/변경
	@Override
	public Cart updateQty(Long cartId, Integer itQty) {
		// TODO Auto-generated method stub
		return null;
	}
	

	// 장바구니 개별 삭제
	@Override
	public Cart deleteCart(Long cartId) {
		return null;
	}

	// 선택된 장바구니 상품 조회
	@Override
	public List<Cart> findByUserNumAndSelectedYn(Long userNum, Character selectedYn) {
		// TODO Auto-generated method stub
		return null;
	}

	// 선택 상품 삭제
	@Override
	public void deleteByUserNumAndSelectedYn(Long userNum, Character selectedYn) {
		
	}

	// 전체 삭제
	@Override
	public void deleteByUserNum(Long userNum) {
		
	}

	// 개별 수량 최대 10개 체크
	@Override
	public void checkQty(Integer itQty) {
		
	}

	// 장바구니 전체 수량 99개 체크
	@Override
	public void totalCheckQty(Long userNum, Integer itQty) {
		
	}

}
