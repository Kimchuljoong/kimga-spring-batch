package kr.co.kimga.batch.domain.transaction.report;

import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository

public class TransactionReportMapRepository {

    private final ConcurrentMap<String, TransactionReport> reportMap = new ConcurrentHashMap<>();

    public void put(TransactionReport transactionReport) {
        String key = getKey(transactionReport);
        reportMap.compute(key, (k, v) -> {
            if (v == null) {
                return transactionReport;
            }
            v.add(transactionReport);
            return v;
        });

    }

    private static String getKey(TransactionReport transactionReport) {
        return transactionReport.getTransactionDate() + "|" + transactionReport.getTransactionType();
    }
}
