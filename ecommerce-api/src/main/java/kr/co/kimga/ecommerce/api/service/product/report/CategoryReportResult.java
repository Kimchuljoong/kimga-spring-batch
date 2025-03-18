package kr.co.kimga.ecommerce.api.service.product.report;

import kr.co.kimga.ecommerce.api.domain.product.report.CategoryReport;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryReportResult {

    private LocalDate statDate;
    private String category;
    private Long productCount;
    private BigDecimal avgSalesPrice;
    private BigDecimal maxSalesPrice;
    private BigDecimal minSalesPrice;
    private Integer totalStockQuantity;
    private BigDecimal potentialSalesAmount;

    public static CategoryReportResult from(CategoryReport categoryReport) {
        return new CategoryReportResult(
                categoryReport.getStatDate(),
                categoryReport.getCategory(),
                categoryReport.getProductCount(),
                categoryReport.getAvgSalesPrice(),
                categoryReport.getMaxSalesPrice(),
                categoryReport.getMinSalesPrice(),
                categoryReport.getTotalStockQuantity(),
                categoryReport.getPotentialSalesAmount()
        );
    }
}
