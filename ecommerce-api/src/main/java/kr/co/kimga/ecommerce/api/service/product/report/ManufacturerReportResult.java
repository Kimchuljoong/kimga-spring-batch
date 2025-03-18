package kr.co.kimga.ecommerce.api.service.product.report;

import kr.co.kimga.ecommerce.api.domain.product.report.ManufacturerReport;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ManufacturerReportResult {

    private LocalDate statDate;
    private String manufacturer;
    private Long productCount;
    private BigDecimal avgSalesPrice;
    private BigDecimal potentialSalesAmount;

    public static ManufacturerReportResult from(ManufacturerReport manufacturerReport) {
        return new ManufacturerReportResult(
                manufacturerReport.getStatDate(),
                manufacturerReport.getManufacturer(),
                manufacturerReport.getProductCount(),
                manufacturerReport.getAvgSalesPrice(),
                manufacturerReport.getPotentialSalesAmount()
        );
    }
}
