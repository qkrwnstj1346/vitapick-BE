package com.vita.vitapickBack.cscenter.ntc;

import java.util.List;

public interface NtcService {
	
	// 전체 공지사항 목록 조회
	List<Ntc> allNtcList();
	
	// use_yn = 'Y' 공지사항 목록 조회
	List<Ntc> UseYNtcList(Character useYn);
	
	// 공지사항 상세 조회
	Ntc selectOne(Long ntcId);
	
	// 공지사항 등록
    Ntc saveNtc(Ntc ntc);

    // 공지사항 수정
    Ntc updateNtc(Long ntcId, Ntc ntc);

	// 공지사항 삭제
	void deleteNtc(Long ntcId) throws Exception;
	
}