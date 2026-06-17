package com.vita.vitapickBack.useraddr;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAddrServiceImpl implements UserAddrService {

	private final UserAddrRepository userAddrRepository;

	// 회원 배송지 목록 조회
	@Override
	@Transactional(readOnly = true)
	public List<UserAddr> findByUserNum(Long userNum) {
		return userAddrRepository.findByUserNum(userNum);
	}

	// 기본 배송지 조회
	@Override
	@Transactional(readOnly = true)
	public UserAddr getBaseAddr(Long userNum) {
		return userAddrRepository.findByUserNumAndBaseYn(userNum, "Y").orElse(null);
	}

	// 배송지 등록
	@Override
	@Transactional
	public UserAddr createAddr(Long userNum, UserAddrDTO dto) {

		int addrCount = userAddrRepository.countByUserNum(userNum);

		if (addrCount >= 10) {
			throw new RuntimeException("배송지는 최대 10개까지 등록할 수 있습니다.");
		}

		String baseYn = "Y".equals(dto.getBaseYn()) ? "Y" : "N";

		if (addrCount == 0) {
			baseYn = "Y";
		}

		if ("Y".equals(baseYn)) {
			UserAddr baseAddr = userAddrRepository.findByUserNumAndBaseYn(userNum, "Y").orElse(null);

			if (baseAddr != null) {
				baseAddr.setBaseYn("N");
			}
		}

		UserAddr addr = UserAddr.builder().userNum(userNum).addrNm(dto.getAddrNm()).rcvNm(dto.getRcvNm())
				.rcvTel(dto.getRcvTel()).zipCd(dto.getZipCd()).addr1(dto.getAddr1()).addr2(dto.getAddr2())
				.baseYn(baseYn).build();

		return userAddrRepository.save(addr);
	}

	// 배송지 수정
	@Override
	public UserAddr updateAddr(Long userNum, Long addrId, UserAddrDTO dto) {

		UserAddr addr = userAddrRepository.findByAddrIdAndUserNum(addrId, userNum)
				.orElseThrow(() -> new RuntimeException("배송지가 존재하지 않습니다."));

		// 기본 배송지 변경
		if ("Y".equals(dto.getBaseYn())) {

			UserAddr baseAddr = userAddrRepository.findByUserNumAndBaseYn(userNum, "Y").orElse(null);

			if (baseAddr != null && !baseAddr.getAddrId().equals(addrId)) {
				baseAddr.setBaseYn("N");
			}

			addr.setBaseYn("Y");
		}

		addr.setAddrNm(dto.getAddrNm());
		addr.setRcvNm(dto.getRcvNm());
		addr.setRcvTel(dto.getRcvTel());
		addr.setZipCd(dto.getZipCd());
		addr.setAddr1(dto.getAddr1());
		addr.setAddr2(dto.getAddr2());

		return addr;
	}

	// 배송지 삭제
	@Override
	public void deleteAddr(Long userNum, Long addrId) {

		UserAddr addr = userAddrRepository.findByAddrIdAndUserNum(addrId, userNum)
				.orElseThrow(() -> new RuntimeException("배송지가 존재하지 않습니다."));

		userAddrRepository.delete(addr);
	}

	// 기본 배송지 변경
	@Override
	public void updateBaseAddr(Long userNum, Long addrId) {

		UserAddr newBaseAddr = userAddrRepository.findByAddrIdAndUserNum(addrId, userNum)
				.orElseThrow(() -> new RuntimeException("배송지가 존재하지 않습니다."));

		UserAddr baseAddr = userAddrRepository.findByUserNumAndBaseYn(userNum, "Y").orElse(null);

		if (baseAddr != null) {
			baseAddr.setBaseYn("N");
		}

		newBaseAddr.setBaseYn("Y");
	}
}