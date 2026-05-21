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

		Cart existCart = findByUserNumAndPrdId(dto.getUserNum(), dto.getPrdId());

		if (existCart != null) {
			Integer newQty = existCart.getItQty() + dto.getItQty();

			checkQty(newQty);
			totalCheckQty(dto.getUserNum(), dto.getItQty());

			existCart.setItQty(newQty);
			return cartRepository.save(existCart);
		}

		Cart cart = Cart.builder().userNum(dto.getUserNum()).prdId(dto.getPrdId()).cusId(dto.getCusId())
				.itQty(dto.getItQty()).selectedYn('Y').build();

		totalCheckQty(dto.getUserNum(), 1);

		return cartRepository.save(cart);
	}

	// 장바구니 수량 증가/감소/변경
	@Override
	public Cart updateQty(Long cartId, Integer itQty) {
		Cart cart = cartRepository.findById(cartId).orElse(null);

		if (cart == null) {
			throw new RuntimeException("장바구니에 담긴 상품이 없습니다.");
		}
		checkQty(itQty);

		cart.setItQty(itQty);

		return cartRepository.save(cart);
	}

	// 장바구니 개별 삭제
	@Override
	public Cart deleteCart(Long cartId) {

		Cart cart = cartRepository.findById(cartId).orElse(null);

		if (cart == null) {
			throw new RuntimeException("장바구니에 담긴 상품이 없습니다.");
		}

		cartRepository.delete(cart);

		return cart;
	}

	// 선택된 장바구니 상품 조회
	@Override
	public List<Cart> findByUserNumAndSelectedYn(Long userNum, Character selectedYn) {
		return cartRepository.findByUserNumAndSelectedYn(userNum, selectedYn);
	}

	// 선택 상품 삭제
	@Override
	public void deleteByUserNumAndSelectedYn(Long userNum, Character selectedYn) {
		cartRepository.deleteByUserNumAndSelectedYn(userNum, selectedYn);
	}

	// 전체 삭제
	@Override
	public void deleteByUserNum(Long userNum) {
		cartRepository.deleteByUserNum(userNum);
	}

	// 개별 수량 최대 10개 체크
	@Override
	public void checkQty(Integer itQty) {

		if (itQty == null || itQty < 1) {
			throw new RuntimeException("상품 수량은 1개 이상이어야 합니다.");
		}

		if (itQty > 10) {
			throw new RuntimeException("상품은 최대 10개까지 담을 수 있습니다.");
		}
	}

	// 장바구니 전체 수량 99개 체크
	@Override
	public void totalCheckQty(Long userNum, Integer itQty) {

		List<Cart> cartList = cartRepository.findByUserNum(userNum);

		int totalQty = 0;

		for (Cart cart : cartList) {
			totalQty += cart.getItQty();
		}

		totalQty += itQty;

		if (totalQty > 99) {
			throw new RuntimeException("장바구니에는 상품을 최대 99개까지 담을 수 있습니다.");
		}
	}

}
