package com.vita.vitapickBack.cscenter.faq;

import java.util.List;

public interface FaqService {

	// FAQ 전체 목록 조회
	List<Faq> findFaqAll();

	// FAQ 카테고리별 조회
	List<Faq> findByFaqCtgCd(String faqCtgCd);

	// use_yn = 'Y' FAQ 조회
	List<Faq> findByUseYn(String useYn);

	// FAQ 상세 조회
	Faq selectOne(Long faqId);

	// FAQ 등록
	Faq saveFaq(FaqDto dto);

	// FAQ 수정
	Faq updateFaq(Long faqId, FaqDto dto);

	// FAQ 삭제
	void deleteFaq(Long faqId);

}