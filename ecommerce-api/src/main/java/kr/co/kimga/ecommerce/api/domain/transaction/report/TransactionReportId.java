package kr.co.kimga.ecommerce.api.domain.transaction.report;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReportId implements Serializable {

    private LocalDate transactionDate;
    private String transactionType;
}
