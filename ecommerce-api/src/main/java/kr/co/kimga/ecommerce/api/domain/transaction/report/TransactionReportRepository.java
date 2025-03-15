package kr.co.kimga.ecommerce.api.domain.transaction.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionReportRepository extends JpaRepository<TransactionReport, TransactionReportId> {

    List<TransactionReport> findAllByTransactionDate(LocalDate transactionDate);
}
