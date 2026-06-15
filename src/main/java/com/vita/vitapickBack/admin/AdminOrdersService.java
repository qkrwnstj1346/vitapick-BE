package com.vita.vitapickBack.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vita.vitapickBack.admin.AdminOrdersResponseDTO.AdminOrderDTO;
import com.vita.vitapickBack.order.Ord;
import com.vita.vitapickBack.order.OrdIt;
import com.vita.vitapickBack.order.OrdItRepository;
import com.vita.vitapickBack.order.OrdRepository;
import com.vita.vitapickBack.users.Users;
import com.vita.vitapickBack.users.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminOrdersService {

    private final OrdRepository ordRepository;
    private final OrdItRepository ordItRepository;
    private final UsersRepository usersRepository;

    public AdminOrdersResponseDTO getOrders(
            int page,
            int size,
            String keyword,
            String status,
            LocalDate startDate,
            LocalDate endDate) {

        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? 10 : Math.min(size, 100);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "ordId"));

        LocalDateTime startAt = startDate == null ? null : startDate.atStartOfDay();
        LocalDateTime endAt = endDate == null ? null : endDate.plusDays(1).atStartOfDay();

        Page<Ord> ordersPage = ordRepository.findAdminOrders(keyword, status, startAt, endAt, pageable);

        return AdminOrdersResponseDTO.builder()
                .content(ordersPage.getContent().stream()
                        .map(this::toAdminOrderDTO)
                        .toList())
                .page(ordersPage.getNumber())
                .size(ordersPage.getSize())
                .totalElements(ordersPage.getTotalElements())
                .totalPages(ordersPage.getTotalPages())
                .build();
    }

    private AdminOrderDTO toAdminOrderDTO(Ord ord) {
        Users buyer = usersRepository.findById(ord.getUserNum()).orElse(null);

        return AdminOrderDTO.builder()
                .orderId(ord.getOrdId())
                .orderNo(ord.getOrdNo())
                .productName(getProductName(ord.getOrdId()))
                .buyerId(buyer == null ? null : buyer.getLoginId())
                .buyerName(buyer == null ? null : buyer.getUserNm())
                .totalPrice(ord.getTotalAmt())
                .orderStatus(ord.getOrdStCd())
                .createdAt(ord.getCrtAt())
                .updatedAt(ord.getUpdAt())
                .build();
    }

    private String getProductName(Long ordId) {
        List<OrdIt> items = ordItRepository.findByOrdId(ordId);
        if (items.isEmpty()) {
            return null;
        }

        String firstName = items.get(0).getPrdNm();
        if (items.size() == 1) {
            return firstName;
        }
        return firstName + " +" + (items.size() - 1);
    }
}
