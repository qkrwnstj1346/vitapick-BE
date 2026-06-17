package com.vita.vitapickBack.admin.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vita.vitapickBack.admin.dto.DashboardSummaryDTO;
import com.vita.vitapickBack.admin.dto.DashboardSummaryDTO.InquiryStatsDTO;
import com.vita.vitapickBack.admin.dto.DashboardSummaryDTO.MemberStatsDTO;
import com.vita.vitapickBack.admin.dto.DashboardSummaryDTO.MonthlyCountDTO;
import com.vita.vitapickBack.admin.dto.DashboardSummaryDTO.PopularCategoryDTO;
import com.vita.vitapickBack.admin.dto.DashboardSummaryDTO.ProductSalesTopDTO;
import com.vita.vitapickBack.admin.repository.AdminInqRepository;
import com.vita.vitapickBack.admin.repository.AdminOrdItRepository;
import com.vita.vitapickBack.admin.repository.AdminOrdRepository;
import com.vita.vitapickBack.admin.repository.AdminUsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private static final String PAID = "PAID";
    private static final String ACTIVE = "ACTIVE";
    private static final String WITHDRAWN = "WITHDRAWN";
    private static final String WAITING = "WAITING";
    private static final String ANSWERED = "ANSWERED";
    private static final int DASHBOARD_MONTH_COUNT = 6;
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private static final Map<Integer, String> CATEGORY_NAMES = Map.ofEntries(
            Map.entry(1, "눈 건강"),
            Map.entry(2, "간 건강"),
            Map.entry(3, "장 건강"),
            Map.entry(4, "피로 회복"),
            Map.entry(5, "면역"),
            Map.entry(6, "피부 건강"),
            Map.entry(7, "혈행 건강"),
            Map.entry(8, "관절 건강"),
            Map.entry(9, "여성 건강"),
            Map.entry(10, "남성 건강"));

    private final AdminOrdRepository adminOrdRepository;
    private final AdminOrdItRepository adminOrdItRepository;
    private final AdminUsersRepository adminUsersRepository;
    private final AdminInqRepository adminInqRepository;

    @Override
    public DashboardSummaryDTO getSummary() {
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime tomorrowStart = today.plusDays(1).atStartOfDay();
        LocalDateTime monthStart = YearMonth.from(today).atDay(1).atStartOfDay();
        LocalDateTime nextMonthStart = YearMonth.from(today).plusMonths(1).atDay(1).atStartOfDay();
        LocalDateTime monthlyStart = YearMonth.from(today).minusMonths(DASHBOARD_MONTH_COUNT - 1).atDay(1).atStartOfDay();

        Long todaySalesAmt = defaultLong(
                adminOrdRepository.sumTotalAmtByOrdStCdAndCrtAtRange(PAID, todayStart, tomorrowStart));
        Long monthSalesAmt = defaultLong(
                adminOrdRepository.sumTotalAmtByOrdStCdAndCrtAtRange(PAID, monthStart, nextMonthStart));
        Long todayPaidOrderCount = defaultLong(
                adminOrdRepository.countByOrdStCdAndCrtAtGreaterThanEqualAndCrtAtLessThan(PAID, todayStart, tomorrowStart));

        return DashboardSummaryDTO.builder()
                .todaySalesAmt(todaySalesAmt)
                .monthSalesAmt(monthSalesAmt)
                .todayPaidOrderCount(todayPaidOrderCount)
                .popularCategory(getPopularCategory(monthStart, nextMonthStart))
                .productSalesTop5(getProductSalesTop5(monthStart, nextMonthStart))
                .inquiryStats(getInquiryStats(todayStart, tomorrowStart))
                .memberStats(getMemberStats())
                .monthlyNewUsers(getMonthlyNewUsers(monthlyStart, nextMonthStart))
                .monthlyPaidOrders(getMonthlyPaidOrders(monthlyStart, nextMonthStart))
                .build();
    }

    private PopularCategoryDTO getPopularCategory(LocalDateTime monthStart, LocalDateTime nextMonthStart) {
        List<Object[]> rows = adminOrdItRepository.findPopularCategorySales(monthStart, nextMonthStart);
        if (rows.isEmpty()) {
            return PopularCategoryDTO.builder()
                    .catCd(null)
                    .catNm(null)
                    .salesAmt(0L)
                    .orderQty(0L)
                    .build();
        }

        Object[] row = rows.get(0);
        Integer catCd = toInteger(row[0]);
        return PopularCategoryDTO.builder()
                .catCd(catCd)
                .catNm(getCategoryName(catCd))
                .salesAmt(toLong(row[1]))
                .orderQty(toLong(row[2]))
                .build();
    }

    private List<ProductSalesTopDTO> getProductSalesTop5(LocalDateTime monthStart, LocalDateTime nextMonthStart) {
        return adminOrdItRepository.findMonthlyProductSalesTop5(monthStart, nextMonthStart).stream()
                .map(row -> {
                    Integer catCd = toInteger(row[2]);
                    return ProductSalesTopDTO.builder()
                            .prdId(toLong(row[0]))
                            .prdNm(row[1] == null ? null : row[1].toString())
                            .catCd(catCd)
                            .catNm(getCategoryName(catCd))
                            .paidQty(toLong(row[3]))
                            .salesAmt(toLong(row[4]))
                            .build();
                })
                .toList();
    }

    private InquiryStatsDTO getInquiryStats(LocalDateTime todayStart, LocalDateTime tomorrowStart) {
        Long waitingCount = defaultLong(adminInqRepository.countByInqStCd(WAITING));
        Long answeredCount = defaultLong(adminInqRepository.countByInqStCd(ANSWERED));
        Long todayNewCount = defaultLong(
                adminInqRepository.countByCrtAtGreaterThanEqualAndCrtAtLessThan(todayStart, tomorrowStart));
        Long totalCount = waitingCount + answeredCount;
        Double answerRate = totalCount == 0L ? 0.0 : answeredCount * 100.0 / totalCount;

        return InquiryStatsDTO.builder()
                .waitingCount(waitingCount)
                .answeredCount(answeredCount)
                .todayNewCount(todayNewCount)
                .answerRate(answerRate)
                .build();
    }

    private MemberStatsDTO getMemberStats() {
        return MemberStatsDTO.builder()
                .totalCount(adminUsersRepository.count())
                .activeCount(defaultLong(adminUsersRepository.countByStatusCd(ACTIVE)))
                .withdrawnCount(defaultLong(adminUsersRepository.countByStatusCd(WITHDRAWN)))
                .build();
    }

    // Dashboard summary monthly new user chart data.
    private List<MonthlyCountDTO> getMonthlyNewUsers(LocalDateTime startAt, LocalDateTime endAt) {
        return toMonthlyCounts(adminUsersRepository.findMonthlyNewUserCounts(startAt, endAt), startAt);
    }

    // Dashboard summary monthly paid order chart data.
    private List<MonthlyCountDTO> getMonthlyPaidOrders(LocalDateTime startAt, LocalDateTime endAt) {
        return toMonthlyCounts(adminOrdRepository.findMonthlyPaidOrderCounts(startAt, endAt), startAt);
    }

    // Dashboard summary fills missing months with zero counts.
    private List<MonthlyCountDTO> toMonthlyCounts(List<Object[]> rows, LocalDateTime startAt) {
        Map<String, Long> countByMonth = rows.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),
                        row -> toLong(row[1]),
                        Long::sum));
        Map<String, Long> recentMonths = new LinkedHashMap<>();
        YearMonth startMonth = YearMonth.from(startAt);
        for (int i = 0; i < DASHBOARD_MONTH_COUNT; i++) {
            String month = startMonth.plusMonths(i).format(MONTH_FORMATTER);
            recentMonths.put(month, countByMonth.getOrDefault(month, 0L));
        }
        return recentMonths.entrySet().stream()
                .map(entry -> MonthlyCountDTO.builder()
                        .month(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .toList();
    }

    private String getCategoryName(Integer catCd) {
        if (catCd == null) {
            return null;
        }
        return CATEGORY_NAMES.getOrDefault(catCd, "카테고리 " + catCd);
    }

    private Long defaultLong(Long value) {
        return value == null ? 0L : value;
    }

    private Long toLong(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof BigInteger bigInteger) {
            return bigInteger.longValue();
        }
        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal.longValue();
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.valueOf(value.toString());
    }

    private Integer toInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.valueOf(value.toString());
    }
}
