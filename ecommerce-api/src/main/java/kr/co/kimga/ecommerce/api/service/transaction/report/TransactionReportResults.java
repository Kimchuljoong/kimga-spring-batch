package kr.co.kimga.ecommerce.api.service.transaction.report;

import kr.co.kimga.ecommerce.api.domain.transaction.report.TransactionReport;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionReportResults {

    private List<TransactionReportResult> transactionReports;

    public static TransactionReportResults from(List<TransactionReport> reports) {
        return new TransactionReportResults(
                reports.stream()
                        .map(TransactionReportResult::from)
                        .toList()
        );
    }
}
