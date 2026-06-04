package com.vita.vitapickBack.useraddr;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAddrServiceImpl implements UserAddrService {

	private final UserAddrRepository userAddrRepository;

	// 회원 배송지 목록 조회
	@Override
	public List<UserAddr> findByUserNum(Long userNum) {

		return userAddrRepository.findByUserNum(userNum);
	}

	// 배송지 등록
	@Override
	public UserAddr createAddr(Long userNum, UserAddrDTO dto) {

		// 첫 배송지면 기본배송지 자동 설정
		String baseYn = "N";

		if (userAddrRepository.countByUserNum(userNum) == 0) {

			baseYn = "Y";
		}

		// 기본배송지 등록 시 기존 기본배송지 해제
		if ("Y".equals(dto.getBaseYn())) {

			UserAddr baseAddr = userAddrRepository.findByUserNumAndBaseYn(userNum, "Y");

			if (baseAddr != null) {

				baseAddr.setBaseYn("N");

				userAddrRepository.save(baseAddr);
			}

			baseYn = "Y";
		}

		UserAddr addr = UserAddr.builder()
				.userNum(userNum)
				.addrNm(dto.getAddrNm())
				.rcvNm(dto.getRcvNm())
				.rcvTel(dto.getRcvTel())
				.zipCd(dto.getZipCd())
				.addr1(dto.getAddr1())
				.addr2(dto.getAddr2())
				.baseYn(baseYn)
				.build();

		return userAddrRepository.save(addr);
	}

	// 배송지 수정
	@Override
	public UserAddr updateAddr(Long userNum, Long addrId, UserAddrDTO dto) {

		UserAddr addr = userAddrRepository.findById(addrId).orElse(null);

		if (addr == null) {

			throw new RuntimeException("배송지가 존재하지 않습니다.");
		}

		// 배송지 주인 확인
		if (!addr.getUserNum().equals(userNum)) {

			throw new RuntimeException("배송지를 수정할 권한이 없습니다.");
		}

		// 기본배송지 변경
		if ("Y".equals(dto.getBaseYn())) {

			UserAddr baseAddr = userAddrRepository.findByUserNumAndBaseYn(userNum, "Y");

			if (baseAddr != null && !baseAddr.getAddrId().equals(addrId)) {

				baseAddr.setBaseYn("N");

				userAddrRepository.save(baseAddr);
			}

			addr.setBaseYn("Y");

		} else {

			addr.setBaseYn("N");
		}

		addr.setAddrNm(dto.getAddrNm());
		addr.setRcvNm(dto.getRcvNm());
		addr.setRcvTel(dto.getRcvTel());
		addr.setZipCd(dto.getZipCd());
		addr.setAddr1(dto.getAddr1());
		addr.setAddr2(dto.getAddr2());

		return userAddrRepository.save(addr);
	}

	// 배송지 삭제
	@Override
	public void deleteAddr(Long userNum, Long addrId) {

		UserAddr addr = userAddrRepository.findById(addrId).orElse(null);

		if (addr == null) {

			throw new RuntimeException("배송지가 존재하지 않습니다.");
		}

		// 배송지 주인 확인
		if (!addr.getUserNum().equals(userNum)) {

			throw new RuntimeException("배송지를 삭제할 권한이 없습니다.");
		}

		userAddrRepository.delete(addr);
	}

	// 기본 배송지 변경
	@Override
	public void updateBaseAddr(Long userNum, Long addrId) {

		UserAddr newBaseAddr = userAddrRepository.findById(addrId).orElse(null);

		if (newBaseAddr == null) {

			throw new RuntimeException("배송지가 존재하지 않습니다.");
		}

		// 배송지 주인 확인
		if (!newBaseAddr.getUserNum().equals(userNum)) {

			throw new RuntimeException("기본 배송지를 변경할 권한이 없습니다.");
		}

		UserAddr baseAddr = userAddrRepository.findByUserNumAndBaseYn(userNum, "Y");

		if (baseAddr != null) {

			baseAddr.setBaseYn("N");

			userAddrRepository.save(baseAddr);
		}

		newBaseAddr.setBaseYn("Y");

		userAddrRepository.save(newBaseAddr);
	}
}