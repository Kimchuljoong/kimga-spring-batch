package kr.co.kimga.ecommerce.api.service.product.report;

import kr.co.kimga.ecommerce.api.domain.product.report.BrandReport;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BrandReportResult {

    private LocalDate statDate;
    private String brand;
    private Long productCount;
    private BigDecimal avgSalesPrice;
    private BigDecimal maxSalesPrice;
    private BigDecimal minSalesPrice;
    private Integer totalStockQuantity;
    private BigDecimal avgStockQuantity;
    private BigDecimal potentialSalesAmount;

    public static BrandReportResult from(BrandReport brandReport) {
        return new BrandReportResult(
                brandReport.getStatDate(),
                brandReport.getBrand(),
                brandReport.getProductCount(),
                brandReport.getAvgSalesPrice(),
                brandReport.getMaxSalesPrice(),
                brandReport.getMinSalesPrice(),
                brandReport.getTotalStockQuantity(),
                brandReport.getAvgStockQuantity(),
                brandReport.getPotentialSalesAmount()
        );
    }
}
