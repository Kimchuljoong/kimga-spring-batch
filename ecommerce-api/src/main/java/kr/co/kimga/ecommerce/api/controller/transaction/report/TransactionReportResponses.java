package kr.co.kimga.ecommerce.api.controller.transaction.report;

import kr.co.kimga.ecommerce.api.service.transaction.report.TransactionReportResults;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionReportResponses {

    private List<TransactionReportResponse> transactionReports;

    public static TransactionReportResponses from(TransactionReportResults results) {
        return new TransactionReportResponses(
                results.getTransactionReports().stream()
                        .map(TransactionReportResponse::from)
                        .toList()
        );
    }
}
