package kr.co.kimga.batch.domain.transaction.report;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TransactionReportTest {

    @Test
    void testAdd() {
        LocalDate now = LocalDate.now();
        TransactionReport report1 = TransactionReport.of(
                now, "ORDER_CREATION", 1L,
                1000L, 1L, 1L,
                1L, new BigDecimal(1L), 100L,
                new HashSet<>(List.of("1")), new HashSet<>(List.of("1")),
                new HashSet<>(List.of("CREDIT_CARD")), 1L);
        TransactionReport report2 = TransactionReport.of(
                now, "ORDER_CREATION", 1L,
                500L, 1L, 1L,
                1L, new BigDecimal(1L), 100L,
                new HashSet<>(List.of("2")), new HashSet<>(List.of("2")),
                new HashSet<>(List.of("CREDIT_CARD")), 2L);

        report1.add(report2);

        assertAll(
                () -> assertThat(report1.getTransactionCount()).isEqualTo(2L),
                () -> assertThat(report1.getTotalAmount()).isEqualTo(1500L),
                () -> assertThat(report1.getCustomerCount()).isEqualTo(2L),
                () -> assertThat(report1.getOrderCount()).isEqualTo(2L),
                () -> assertThat(report1.getPaymentMethodCount()).isEqualTo(1L),
                () -> assertThat(report1.getAvgProductCount()).isEqualTo(new BigDecimal("1.5"))
        );
    }

}