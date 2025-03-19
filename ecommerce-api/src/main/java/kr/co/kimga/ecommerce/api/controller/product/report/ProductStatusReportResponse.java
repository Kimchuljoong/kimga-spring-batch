package kr.co.kimga.ecommerce.api.controller.product.report;

import kr.co.kimga.ecommerce.api.service.product.report.ProductStatusReportResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductStatusReportResponse {

    private LocalDate statDate;
    private String productStatus;
    private Long productCount;
    private BigDecimal avgStockQuantity;

    public static ProductStatusReportResponse from(ProductStatusReportResult result) {
        return new ProductStatusReportResponse(
                result.getStatDate(),
                result.getProductStatus(),
                result.getProductCount(),
                result.getAvgStockQuantity()
        );
    }
}
