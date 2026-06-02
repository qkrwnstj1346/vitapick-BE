package com.vita.vitapickBack.cscenter.faq;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

	private final FaqRepository faqRepository;

	// FAQ 전체 목록 조회
	// = 관리자 FAQ 목록 조회
	// = use_yn Y/N 전체 조회
	@Override
	public List<Faq> findFaqAll() {

		return faqRepository.findAll();
	}

	// FAQ 상세 조회
	// = FAQ 번호(faqId) 기준 단건 조회
	@Override
	public Faq selectOne(Long faqId) {

		return faqRepository.findById(faqId)
				.orElseThrow(() ->
						new RuntimeException("FAQ 상세 정보를 불러오지 못했습니다."));
	}

	// FAQ 등록
	// = 관리자만 가능
	@Override
	public Faq saveFaq(FaqDto dto) {

		// 제목 또는 내용 미입력 체크
		if (dto.getTtl() == null || dto.getTtl().isBlank()
				|| dto.getFaqTxt() == null || dto.getFaqTxt().isBlank()) {

			throw new RuntimeException("FAQ 제목과 내용을 입력해주세요.");
		}

		Faq faq = new Faq();

		faq.setFaqCtgCd(dto.getFaqCtgCd());
		faq.setTtl(dto.getTtl());
		faq.setFaqTxt(dto.getFaqTxt());
		faq.setViewCnt(0);
		faq.setUseYn("Y");

		return faqRepository.save(faq);
	}

	// FAQ 수정
	// = 관리자만 가능
	@Override
	public Faq updateFaq(Long faqId, FaqDto dto) {

		Faq dbFaq = faqRepository.findById(faqId)
				.orElseThrow(() ->
						new RuntimeException("FAQ 상세 정보를 불러오지 못했습니다."));

		dbFaq.setFaqCtgCd(dto.getFaqCtgCd());
		dbFaq.setTtl(dto.getTtl());
		dbFaq.setFaqTxt(dto.getFaqTxt());

		return faqRepository.save(dbFaq);
	}

	// FAQ 삭제
	// = 관리자만 가능
	@Override
	public void deleteFaq(Long faqId) {

		faqRepository.deleteById(faqId);
	}

	// FAQ 카테고리별 조회
	// = 관리자 카테고리별 FAQ 조회
	// = 해당 카테고리의 use_yn Y/N 전체 조회
	@Override
	public List<Faq> findByFaqCtgCd(String faqCtgCd) {

		return faqRepository.findByFaqCtgCd(faqCtgCd);
	}

	// use_yn = 'Y' FAQ 조회
	// = 일반회원 FAQ 목록 조회
	// = 공개 FAQ만 조회
	@Override
	public List<Faq> findByUseYn(String useYn) {

		return faqRepository.findByUseYn(useYn);
	}

	// FAQ 카테고리 + use_yn 조회
	// = 일반회원 카테고리별 FAQ 조회
	// = 공개 FAQ만 조회
	@Override
	public List<Faq> findByFaqCtgCdAndUseYn(
			String faqCtgCd,
			String useYn) {

		return faqRepository.findByFaqCtgCdAndUseYn(
				faqCtgCd,
				useYn);
	}
}