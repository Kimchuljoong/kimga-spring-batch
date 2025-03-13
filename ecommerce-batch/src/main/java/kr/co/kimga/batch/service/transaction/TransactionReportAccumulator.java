package kr.co.kimga.batch.service.transaction;

import kr.co.kimga.batch.domain.transaction.report.TransactionReport;
import kr.co.kimga.batch.domain.transaction.report.TransactionReportMapRepository;
import kr.co.kimga.batch.dto.transaction.TransactionLog;
import org.springframework.stereotype.Component;

@Component
public class TransactionReportAccumulator {

    private final TransactionReportMapRepository repository;

    public void accumulate(TransactionLog transactionLog) {
        if (!"SUCCESS".equalsIgnoreCase(transactionLog.getTransactionStatus())) {
            return;
        }

        repository.put(TransactionReport.from(transactionLog));
    }
}
