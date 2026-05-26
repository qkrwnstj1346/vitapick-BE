package com.vita.vitapickBack.order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vita.vitapickBack.cart.Cart;
import com.vita.vitapickBack.cart.CartRepository;
import com.vita.vitapickBack.products.prd.Prd;
import com.vita.vitapickBack.products.prd.PrdRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdServiceImpl implements OrdService {

	private final OrdRepository ordRepository;
	private final OrdItRepository ordItRepository;
	private final PayRepository payRepository;
	private final CartRepository cartRepository;
	private final PrdRepository prdRepository;

	// 회원 주문 목록 조회
	@Override
	public List<Ord> findByUserNum(Long userNum) {
		return ordRepository.findByUserNum(userNum);
	}

	// 주문번호 조회
	@Override
	public Ord findByOrdNo(String ordNo) {
		return ordRepository.findByOrdNo(ordNo);
	}

	// 주문별 주문상품 목록 조회
	@Override
	public List<OrdIt> findOrdItByOrdId(Long ordId) {
		return ordItRepository.findByOrdId(ordId);
	}

	// 상품별 주문 조회
	@Override
	public List<OrdIt> findByPrdId(Long prdId) {
		return ordItRepository.findByPrdId(prdId);
	}

	// 주문번호로 결제 조회
	@Override
	public Pay findPayByOrdId(Long ordId) {
		return payRepository.findByOrdId(ordId);
	}

	// 결제번호 조회
	@Override
	public Pay findByPayNo(String payNo) {
		return payRepository.findByPayNo(payNo);
	}

	// 주문 생성 + 결제
	@Override
	public Ord createOrder(OrdDTO orddto) {

		// 배송지 선택 체크
		if (orddto.getAddrId() == null) {
			throw new RuntimeException("배송지를 선택해주세요.");
		}

		// 결제수단 선택 체크
		if (orddto.getPayDto() == null || orddto.getPayDto().getPayMthdCd() == null) {

			throw new RuntimeException("결제수단을 선택해주세요.");
		}

		// 주문번호 생성
		String ordNo = "ORD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

		// 결제번호 생성
		String payNo = "PAY" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

		// 주문 저장
		Ord ord = Ord.builder().userNum(orddto.getUserNum()).ordNo(ordNo).addrId(orddto.getAddrId())
				.totalAmt(orddto.getTotalAmt()).ordStCd("PAID").build();

		Ord savedOrd = ordRepository.save(ord);

		// 상품상세 바로구매
		if (orddto.getPrdList() != null && !orddto.getPrdList().isEmpty()) {

			// 주문상품 저장
			for (OrdItDTO prdDto : orddto.getPrdList()) {

				Integer itQty = prdDto.getItQty();
				Integer price = prdDto.getPrice();
				Integer itAmt = price * itQty;

				OrdIt ordIt = OrdIt.builder().ordId(savedOrd.getOrdId()).prdId(prdDto.getPrdId())
						.cusId(prdDto.getCusId()).prdNm(prdDto.getPrdNm()).itQty(itQty).price(price).itAmt(itAmt)
						.build();

				ordItRepository.save(ordIt);
			}

		} else {

			// 장바구니 선택 상품 조회
			List<Cart> cartList = cartRepository.findByUserNumAndSelectedYn(orddto.getUserNum(), 'Y');

			// 선택된 장바구니 상품 없을 경우
			if (cartList == null || cartList.isEmpty()) {

				throw new RuntimeException("선택된 장바구니 상품이 없습니다.");
			}

			// 장바구니 선택 상품 주문상품으로 저장
			for (Cart cart : cartList) {

				// 상품 정보 조회
				Prd prd = prdRepository.findById(cart.getPrdId()).orElse(null);

				// 상품 정보 없을 경우
				if (prd == null) {

					throw new RuntimeException("상품 정보를 찾을 수 없습니다.");
				}

				Integer itQty = cart.getItQty();
				Integer price = prd.getPrice();
				Integer itAmt = price * itQty;

				// 주문상품 저장
				OrdIt ordIt = OrdIt.builder().ordId(savedOrd.getOrdId()).prdId(cart.getPrdId()).cusId(cart.getCusId())
						.prdNm(prd.getPrdNm()).itQty(itQty).price(price).itAmt(itAmt).build();

				ordItRepository.save(ordIt);
			}

			// 주문 완료된 장바구니 선택 상품 삭제
			cartRepository.deleteByUserNumAndSelectedYn(orddto.getUserNum(), 'Y');
		}

		// 결제 저장
		Pay pay = Pay.builder().ordId(savedOrd.getOrdId()).payNo(payNo).payMthdCd(orddto.getPayDto().getPayMthdCd())
				.payAmt(savedOrd.getTotalAmt()).payStCd("PAID").paidAt(LocalDateTime.now()).build();

		payRepository.save(pay);

		// 생성된 주문 반환
		return savedOrd;
	}
}