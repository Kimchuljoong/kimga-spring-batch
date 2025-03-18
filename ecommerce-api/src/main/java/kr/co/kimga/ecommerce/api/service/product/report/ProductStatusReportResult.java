package kr.co.kimga.ecommerce.api.service.product.report;

import kr.co.kimga.ecommerce.api.domain.product.report.ProductStatusReport;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductStatusReportResult {

    private LocalDate statDate;
    private String productStatus;
    private Long productCount;
    private BigDecimal avgStockQuantity;

    public static ProductStatusReportResult from(ProductStatusReport productStatusReport) {
        return new ProductStatusReportResult(
                productStatusReport.getStatDate(),
                productStatusReport.getProductStatus(),
                productStatusReport.getProductCount(),
                productStatusReport.getAvgStockQuantity()
        );
    }
}
