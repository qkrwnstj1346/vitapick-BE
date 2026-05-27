package com.vita.vitapickBack.cart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {

	// 회원 장바구니 목록 조회
	List<Cart> findByUserNum(Long userNum);

	// 장바구니 화면 상품 노출
	@Query("""
			SELECT new com.vita.vitapickBack.cart.CartDTO(
				c.cartId,
				c.userNum,
				c.prdId,
				c.cusId,
				c.itQty,
				c.selectedYn,
				c.crtAt,
				c.updAt,
				p.prdNm,
				p.price,
				p.brand,
				pi.imgUrl
			)
			FROM Cart c, Prd p, PrdImg pi
			WHERE c.prdId = p.prdId
			AND p.prdId = pi.prdId
			AND pi.imgTypeCd = 'THUMB'
			AND c.userNum = :userNum
			ORDER BY c.cusId DESC, c.cartId DESC
			""")
	List<CartDTO> findCartListWithProduct(@Param("userNum") Long userNum);
	// 동일 상품 체크
	// 같은 커스텀(cus_id) 안에서 같은 상품이면 수량 증가
	Cart findByUserNumAndCusIdAndPrdId(Long userNum, Long cusId, Long prdId);

	// 일반 상품 동일 상품 체크
	// cus_id가 없는 일반 상품이면 user_num + prd_id로 체크
	Cart findByUserNumAndCusIdIsNullAndPrdId(Long userNum, Long prdId);

	// 장바구니 수량 증가/감소/변경
	// 기본 제공 메서드 사용
	// findById(cartId)
	// save(cart)

	// 개별 삭제
	// 기본 제공 메서드 사용
	// deleteById(cartId)

	// 선택된 장바구니 상품 조회
	// 선택 상품 삭제 및 결제하기 시 사용
	List<Cart> findByUserNumAndSelectedYn(Long userNum, Character selectedYn);

	// 선택 상품 삭제
	void deleteByUserNumAndSelectedYn(Long userNum, Character selectedYn);

	// 전체 삭제
	void deleteByUserNum(Long userNum);

	// 개별 수량 최대 10개 체크
	// Service에서 itQty 값 체크

	// 장바구니 전체 수량 99개 체크
	// Service에서 장바구니 전체 수량 합계 계산

}