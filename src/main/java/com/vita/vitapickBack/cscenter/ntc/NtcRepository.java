package com.vita.vitapickBack.cscenter.ntc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NtcRepository extends JpaRepository<Ntc, Long> {

	// use_yn = 'Y' 인 공지사항 목록 조회
	List<Ntc> findByUseYn(Character useYn);
}
