package com.vita.vitapickBack.order;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdServiceImpl implements OrdService {

	private final OrdRepository ordRepository;
	private final OrdItRepository ordItRepository;
	private final PayRepository payRepository;

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

		// 주문 저장
		Ord ord = Ord.builder().userNum(orddto.getUserNum()).addrId(orddto.getAddrId()).totalAmt(orddto.getTotalAmt())
				.ordStCd("PAID").build();
		Ord savedOrd = ordRepository.save(ord);

		// 주문 상품 저장
		for (OrdItDTO prdDto : orddto.getPrdList()) {
			OrdIt ordIt = OrdIt.builder().ordId(savedOrd.getOrdId()).prdId(prdDto.getPrdId()).itQty(prdDto.getItQty())
					.build();
			ordItRepository.save(ordIt);
		}

		// 결제 저장
		Pay pay = Pay.builder().ordId(savedOrd.getOrdId()).payMthdCd(orddto.getPaydto().getPayMthdCd())
				.payAmt(savedOrd.getTotalAmt()).payStCd("PAID").build();
		payRepository.save(pay);

		return savedOrd;
	}

}
