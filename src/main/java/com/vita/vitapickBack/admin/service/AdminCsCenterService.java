package com.vita.vitapickBack.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vita.vitapickBack.admin.repository.AdminFaqRepository;
import com.vita.vitapickBack.cscenter.faq.Faq;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCsCenterService {

    private final AdminFaqRepository adminFaqRepository;

    public Page<Faq> getFaqs(int page, int size, String useYn, String faqCtgCd, String sort) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 10 : Math.min(size, 100);
        Sort.Direction direction = "oldest".equalsIgnoreCase(sort) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(direction, "crtAt"));

        return adminFaqRepository.findAdminFaqs(useYn, faqCtgCd, pageable);
    }
}
