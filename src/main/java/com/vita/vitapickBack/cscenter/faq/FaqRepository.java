package com.vita.vitapickBack.cscenter.faq;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {

	// FAQ 카테고리별 조회
	List<Faq> findByFaqCtgCd(String faqCtgCd);

	// use_yn = 'Y' FAQ 조회
	List<Faq> findByUseYn(String useYn);

}