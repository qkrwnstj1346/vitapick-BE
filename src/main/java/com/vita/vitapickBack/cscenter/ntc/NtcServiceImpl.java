package com.vita.vitapickBack.cscenter.ntc;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NtcServiceImpl implements NtcService {

    private final NtcRepository repository;

    // 전체 공지사항 목록 조회
    // = 관리자 공지사항 목록 조회
    // = use_yn Y/N 전체 조회
    @Override
    public List<Ntc> allNtcList() {

        return repository.findAll();
    }

    // use_yn = 'Y' 공지사항 목록 조회
    // = 일반회원 공지사항 목록 조회
    // = 공개 공지사항만 조회
    @Override
    public List<Ntc> useYNtcList(Character useYn) {

        return repository.findByUseYn(useYn);
    }

    // 공지사항 상세 조회
    // = 공지사항 번호(ntcId) 기준 단건 조회
    @Override
    public Ntc selectOne(Long ntcId) {

        return repository.findById(ntcId)
                .orElseThrow(() ->
                        new RuntimeException("공지사항이 존재하지 않습니다."));
    }

    // 공지사항 등록
    // = 관리자만 가능
    @Override
    public Ntc saveNtc(Ntc ntc) {

        // 제목 또는 내용 미입력 체크
        if (ntc.getTtl() == null || ntc.getTtl().isBlank()
                || ntc.getNtcTxt() == null || ntc.getNtcTxt().isBlank()) {

            throw new RuntimeException("공지사항 제목과 내용을 입력해주세요.");
        }

        // 조회수 기본값 세팅
        if (ntc.getViewCnt() == null) {
            ntc.setViewCnt(0);
        }

        // 사용 여부 기본값 세팅
        if (ntc.getUseYn() == null) {
            ntc.setUseYn('Y');
        }

        return repository.save(ntc);
    }

    // 공지사항 수정
    // = 관리자만 가능
    @Override
    public Ntc updateNtc(Long ntcId, Ntc ntc) {

        Ntc dbNtc = repository.findById(ntcId)
                .orElseThrow(() ->
                        new RuntimeException("공지사항이 존재하지 않습니다."));

        dbNtc.setTtl(ntc.getTtl());
        dbNtc.setNtcTxt(ntc.getNtcTxt());
        dbNtc.setUseYn(ntc.getUseYn());

        return repository.save(dbNtc);
    }

    // 공지사항 삭제
    // = 관리자만 가능
    @Override
    public void deleteNtc(Long ntcId) {

        repository.deleteById(ntcId);
    }
}