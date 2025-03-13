package kr.co.kimga.batch.domain.transaction.report;

import jakarta.persistence.Transient;
import kr.co.kimga.batch.dto.transaction.TransactionLog;
import kr.co.kimga.batch.util.DateTimeUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionReport {

    private LocalDate transactionDate;
    private String transactionType;

    private Long transactionCount;
    private Long totalAmount;
    private Long customerCount;
    private Long orderCount;

    private Long paymentMethodCount;
    private BigDecimal avgProductCount;
    private Long totalItemQuantity;

    @Transient
    private Set<String> customerSet;

    @Transient
    private Set<String> orderSet;

    @Transient
    private Set<String> paymentMethodSet;

    @Transient
    private Long sumProductCount;


    public static TransactionReport from(TransactionLog transactionLog) {
        return new TransactionReport(
                DateTimeUtils.toLocalDateTime(transactionLog.getTimestamp()).toLocalDate(),
                transactionLog.getTransactionType(),
                1L,
                Long.parseLong(transactionLog.getTotalAmount()),
                1L,
                1L,
                1L,
                new BigDecimal(Long.parseLong(transactionLog.getProductCount())),
                Long.parseLong(transactionLog.getTotalItemQuantity()),
                new HashSet<>(List.of(transactionLog.getCustomerId())),
                new HashSet<>(List.of(transactionLog.getOrderId())),
                new HashSet<>(List.of(transactionLog.getPaymentMethod())),
                Long.parseLong(transactionLog.getProductCount())
        );
    }

    public void add(TransactionReport transactionReport) {
        transactionCount += transactionReport.getTransactionCount();
        totalAmount += transactionReport.getTotalAmount();
        customerSet.addAll(transactionReport.getCustomerSet());
        orderSet.addAll(transactionReport.getOrderSet());
        paymentMethodSet.addAll(transactionReport.getPaymentMethodSet());
        customerCount = (long) customerSet.size();
        orderCount = (long) orderSet.size();
        paymentMethodCount = (long) paymentMethodSet.size();
        sumProductCount += transactionReport.getSumProductCount();
        avgProductCount = new BigDecimal((double) sumProductCount / transactionCount);
        totalAmount += transactionReport.getTotalAmount();
    }
}
