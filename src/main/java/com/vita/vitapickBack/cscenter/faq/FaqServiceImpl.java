package com.vita.vitapickBack.cscenter.faq;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

	private final FaqRepository faqRepository;

	// FAQ 전체 목록 조회
	@Override
	public List<Faq> findFaqAll() {
		return faqRepository.findAll();
	}

	// FAQ 카테고리별 조회
	@Override
	public List<Faq> findByFaqCtgCd(String faqCtgCd) {
		return faqRepository.findByFaqCtgCd(faqCtgCd);
	}

	// use_yn = 'Y' FAQ 조회
	@Override
	public List<Faq> findByUseYn(String useYn) {
		return faqRepository.findByUseYn(useYn);
	}

	// FAQ 상세 조회
	@Override
	public Faq selectOne(Long faqId) {

		Optional<Faq> result = faqRepository.findById(faqId);

		if (result.isPresent()) {
			return result.get();
		}

		throw new RuntimeException("FAQ 상세 정보를 불러오지 못했습니다.");
	}

	// FAQ 등록
	@Override
	public Faq saveFaq(FaqDto dto) {

		// 제목 또는 내용 미입력 체크
		if (dto.getTtl() == null || dto.getTtl().isBlank()
				|| dto.getFaqTxt() == null || dto.getFaqTxt().isBlank()) {

			throw new RuntimeException("FAQ 제목과 내용을 입력해주세요.");
		}

		Faq faq = new Faq();

		// 기본값 세팅
		faq.setFaqCtgCd(dto.getFaqCtgCd());
		faq.setTtl(dto.getTtl());
		faq.setFaqTxt(dto.getFaqTxt());
		faq.setViewCnt(0);
		faq.setUseYn("Y");
		faq.setCrtAt(LocalDateTime.now());

		// DB 저장
		return faqRepository.save(faq);
	}

	// FAQ 수정
	@Override
	public Faq updateFaq(Long faqId, FaqDto dto) {

		Optional<Faq> result = faqRepository.findById(faqId);

		if (result.isPresent()) {

			Faq dbFaq = result.get();

			dbFaq.setFaqCtgCd(dto.getFaqCtgCd());
			dbFaq.setTtl(dto.getTtl());
			dbFaq.setFaqTxt(dto.getFaqTxt());
			dbFaq.setUpdAt(LocalDateTime.now());

			return faqRepository.save(dbFaq);
		}

		throw new RuntimeException("FAQ 상세 정보를 불러오지 못했습니다.");
	}

	// FAQ 삭제
	@Override
	public void deleteFaq(Long faqId) {

		Optional<Faq> result = faqRepository.findById(faqId);

		if (result.isPresent()) {

			Faq dbFaq = result.get();

			faqRepository.delete(dbFaq);

			return;
		}

		throw new RuntimeException("삭제할 FAQ가 존재하지 않습니다.");
	}

}