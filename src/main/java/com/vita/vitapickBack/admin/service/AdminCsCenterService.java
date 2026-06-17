package com.vita.vitapickBack.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vita.vitapickBack.admin.repository.AdminFaqRepository;
import com.vita.vitapickBack.admin.repository.AdminNtcRepository;
import com.vita.vitapickBack.cscenter.faq.Faq;
import com.vita.vitapickBack.cscenter.faq.FaqDto;
import com.vita.vitapickBack.cscenter.ntc.Ntc;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCsCenterService {

    private final AdminFaqRepository adminFaqRepository;
    private final AdminNtcRepository adminNtcRepository;

    public Page<Ntc> getNotices(int page, int size, String useYn) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 10 : Math.min(size, 100);
        Pageable pageable = PageRequest.of(safePage, safeSize,
                Sort.by(Sort.Direction.DESC, "crtAt").and(Sort.by(Sort.Direction.DESC, "ntcId")));

        Character noticeUseYn = useYn == null || useYn.isBlank() ? null : useYn.charAt(0);
        return adminNtcRepository.findAdminNotices(noticeUseYn, pageable);
    }

    public Page<Faq> getFaqs(int page, int size, String useYn, String faqCtgCd, String sort) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 10 : Math.min(size, 100);
        Pageable pageable = PageRequest.of(safePage, safeSize,
                Sort.by(Sort.Direction.DESC, "crtAt").and(Sort.by(Sort.Direction.DESC, "faqId")));

        List<String> faqCtgCds = toFaqCategoryCodes(faqCtgCd);
        if (faqCtgCds == null) {
            return adminFaqRepository.findAdminFaqs(useYn, pageable);
        }
        return adminFaqRepository.findAdminFaqsByCategoryCodes(useYn, faqCtgCds, pageable);
    }

    @Transactional
    public Faq createFaq(FaqDto dto) {
        Faq faq = new Faq();
        faq.setFaqCtgCd(dto.getFaqCtgCd());
        faq.setTtl(dto.getTtl());
        faq.setFaqTxt(dto.getFaqTxt());
        faq.setViewCnt(0);
        faq.setUseYn(dto.getUseYn() == null || dto.getUseYn().isBlank() ? "Y" : dto.getUseYn());

        return adminFaqRepository.save(faq);
    }

    private List<String> toFaqCategoryCodes(String faqCtgCd) {
        if (faqCtgCd == null || faqCtgCd.isBlank()) {
            return null;
        }
        return switch (faqCtgCd) {
            case "\uC8FC\uBB38/\uBC30\uC1A1" -> List.of("\uC8FC\uBB38/\uBC30\uC1A1", "\uC8FC\uBB38", "\uBC30\uC1A1", "ORDER_DELIVERY", "ORDER", "DELIVERY");
            case "\uC8FC\uBB38" -> List.of("\uC8FC\uBB38", "ORDER");
            case "\uBC30\uC1A1" -> List.of("\uBC30\uC1A1", "DELIVERY");
            case "\uC0C1\uD488" -> List.of("\uC0C1\uD488", "PRODUCT", "PRD");
            case "\uD68C\uC6D0" -> List.of("\uD68C\uC6D0", "USER", "MEMBER");
            case "\uAE30\uD0C0" -> List.of("\uAE30\uD0C0", "ETC");
            default -> List.of(faqCtgCd);
        };
    }
}
