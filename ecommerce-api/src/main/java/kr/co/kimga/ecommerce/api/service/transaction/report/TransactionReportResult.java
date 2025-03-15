package kr.co.kimga.ecommerce.api.service.transaction.report;

import kr.co.kimga.ecommerce.api.domain.transaction.report.TransactionReport;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionReportResult {

    private LocalDate transactionDate;
    private String transactionType;

    private Long transactionCount;
    private Long totalAmount;
    private Long customerCount;
    private Long orderCount;
    private Long paymentMethodCount;
    private BigDecimal avgProductCount;
    private Long totalItemQuantity;

    public static TransactionReportResult from(TransactionReport report) {
        return new TransactionReportResult(
                report.getTransactionDate(),
                report.getTransactionType(),
                report.getTransactionCount(),
                report.getTotalAmount(),
                report.getCustomerCount(),
                report.getOrderCount(),
                report.getPaymentMethodCount(),
                report.getAvgProductCount(),
                report.getTotalItemQuantity()
        );
    }
}
