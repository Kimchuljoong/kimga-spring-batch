package kr.co.kimga.ecommerce.api.service.transaction.report;

import kr.co.kimga.ecommerce.api.domain.transaction.report.TransactionReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransactionReportService {

    private final TransactionReportRepository repository;

    public TransactionReportResults findByDate(LocalDate date) {
        return TransactionReportResults.from(repository.findAllByTransactionDate(date));
    }
}
