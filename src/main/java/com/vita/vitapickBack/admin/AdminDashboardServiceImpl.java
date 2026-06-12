package com.vita.vitapickBack.admin;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vita.vitapickBack.admin.DashboardSummaryDTO.InquiryStatsDTO;
import com.vita.vitapickBack.admin.DashboardSummaryDTO.MemberStatsDTO;
import com.vita.vitapickBack.admin.DashboardSummaryDTO.PopularCategoryDTO;
import com.vita.vitapickBack.admin.DashboardSummaryDTO.ProductSalesTopDTO;
import com.vita.vitapickBack.cscenter.inq.InqRepository;
import com.vita.vitapickBack.order.OrdItRepository;
import com.vita.vitapickBack.order.OrdRepository;
import com.vita.vitapickBack.users.UsersRepository;

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

    private final OrdRepository ordRepository;
    private final OrdItRepository ordItRepository;
    private final UsersRepository usersRepository;
    private final InqRepository inqRepository;

    @Override
    public DashboardSummaryDTO getSummary() {
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime tomorrowStart = today.plusDays(1).atStartOfDay();
        LocalDateTime monthStart = YearMonth.from(today).atDay(1).atStartOfDay();
        LocalDateTime nextMonthStart = YearMonth.from(today).plusMonths(1).atDay(1).atStartOfDay();

        Long todaySalesAmt = defaultLong(
                ordRepository.sumTotalAmtByOrdStCdAndCrtAtRange(PAID, todayStart, tomorrowStart));
        Long monthSalesAmt = defaultLong(
                ordRepository.sumTotalAmtByOrdStCdAndCrtAtRange(PAID, monthStart, nextMonthStart));
        Long todayPaidOrderCount = defaultLong(
                ordRepository.countByOrdStCdAndCrtAtGreaterThanEqualAndCrtAtLessThan(PAID, todayStart, tomorrowStart));

        return DashboardSummaryDTO.builder()
                .todaySalesAmt(todaySalesAmt)
                .monthSalesAmt(monthSalesAmt)
                .todayPaidOrderCount(todayPaidOrderCount)
                .popularCategory(getPopularCategory(monthStart, nextMonthStart))
                .productSalesTop5(getProductSalesTop5(monthStart, nextMonthStart))
                .inquiryStats(getInquiryStats(todayStart, tomorrowStart))
                .memberStats(getMemberStats())
                .build();
    }

    private PopularCategoryDTO getPopularCategory(LocalDateTime monthStart, LocalDateTime nextMonthStart) {
        List<Object[]> rows = ordItRepository.findPopularCategorySales(monthStart, nextMonthStart);
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
        return ordItRepository.findMonthlyProductSalesTop5(monthStart, nextMonthStart).stream()
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
        Long waitingCount = defaultLong(inqRepository.countByInqStCd(WAITING));
        Long answeredCount = defaultLong(inqRepository.countByInqStCd(ANSWERED));
        Long todayNewCount = defaultLong(
                inqRepository.countByCrtAtGreaterThanEqualAndCrtAtLessThan(todayStart, tomorrowStart));
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
                .totalCount(usersRepository.count())
                .activeCount(defaultLong(usersRepository.countByStatusCd(ACTIVE)))
                .withdrawnCount(defaultLong(usersRepository.countByStatusCd(WITHDRAWN)))
                .build();
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
