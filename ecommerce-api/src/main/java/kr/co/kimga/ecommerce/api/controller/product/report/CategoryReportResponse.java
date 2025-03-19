package kr.co.kimga.ecommerce.api.controller.product.report;

import kr.co.kimga.ecommerce.api.service.product.report.CategoryReportResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryReportResponse {

    private LocalDate statDate;
    private String category;
    private Long productCount;
    private BigDecimal avgSalesPrice;
    private BigDecimal maxSalesPrice;
    private BigDecimal minSalesPrice;
    private Integer totalStockQuantity;
    private BigDecimal potentialSalesAmount;

    public static CategoryReportResponse from(CategoryReportResult result) {
        return new CategoryReportResponse(
                result.getStatDate(),
                result.getCategory(),
                result.getProductCount(),
                result.getAvgSalesPrice(),
                result.getMaxSalesPrice(),
                result.getMinSalesPrice(),
                result.getTotalStockQuantity(),
                result.getPotentialSalesAmount()
        );
    }
}
