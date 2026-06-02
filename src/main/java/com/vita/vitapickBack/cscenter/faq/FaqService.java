package com.vita.vitapickBack.cscenter.faq;

import java.util.List;

public interface FaqService {

	// FAQ 전체 목록 조회
	// = 관리자 FAQ 목록 조회
	// = use_yn Y/N 전체 조회
	List<Faq> findFaqAll();

	// FAQ 상세 조회
	// = FAQ 번호(faqId) 기준 단건 조회
	Faq selectOne(Long faqId);

	// FAQ 등록
	// = 관리자만 가능
	Faq saveFaq(FaqDto dto);

	// FAQ 수정
	// = 관리자만 가능
	Faq updateFaq(Long faqId, FaqDto dto);

	// FAQ 삭제
	// = 관리자만 가능
	void deleteFaq(Long faqId);

	// FAQ 카테고리별 조회
	// = 카테고리 선택 시 조회
	List<Faq> findByFaqCtgCd(String faqCtgCd);

	// use_yn = 'Y' FAQ 조회
	// = 일반회원 FAQ 목록 조회
	// = 공개 FAQ만 조회
	List<Faq> findByUseYn(String useYn);

	// FAQ 카테고리 + use_yn 조회
	// = 일반회원 카테고리별 FAQ 조회
	// = 공개 FAQ만 조회
	List<Faq> findByFaqCtgCdAndUseYn(String faqCtgCd, String useYn);

}