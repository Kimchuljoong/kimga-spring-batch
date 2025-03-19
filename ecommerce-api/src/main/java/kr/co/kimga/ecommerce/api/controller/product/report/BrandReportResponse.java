package kr.co.kimga.ecommerce.api.controller.product.report;

import kr.co.kimga.ecommerce.api.service.product.report.BrandReportResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BrandReportResponse {

    private LocalDate statDate;
    private String brand;
    private Long productCount;
    private BigDecimal avgSalesPrice;
    private BigDecimal maxSalesPrice;
    private BigDecimal minSalesPrice;
    private Integer totalStockQuantity;
    private BigDecimal avgStockQuantity;
    private BigDecimal potentialSalesAmount;

    public static BrandReportResponse from(BrandReportResult result) {
        return new BrandReportResponse(
                result.getStatDate(),
                result.getBrand(),
                result.getProductCount(),
                result.getAvgSalesPrice(),
                result.getMaxSalesPrice(),
                result.getMinSalesPrice(),
                result.getTotalStockQuantity(),
                result.getAvgStockQuantity(),
                result.getPotentialSalesAmount()
        );
    }
}
