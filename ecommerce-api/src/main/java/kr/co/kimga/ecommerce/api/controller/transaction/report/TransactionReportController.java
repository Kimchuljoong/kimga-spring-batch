package kr.co.kimga.ecommerce.api.controller.transaction.report;

import kr.co.kimga.ecommerce.api.service.transaction.report.TransactionReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/transaction/reports")
@RequiredArgsConstructor
public class TransactionReportController {

    private final TransactionReportService transactionReportService;

    @GetMapping("")
    public TransactionReportResponses getTransactionReports(
            @RequestParam("dt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return TransactionReportResponses.from(transactionReportService.findByDate(date));
    }
}
