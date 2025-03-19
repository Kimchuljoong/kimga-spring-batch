package kr.co.kimga.ecommerce.api.controller.product.report;

import kr.co.kimga.ecommerce.api.service.product.report.ManufacturerReportResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ManufacturerReportResponse {

    private LocalDate statDate;
    private String manufacturer;
    private Long productCount;
    private BigDecimal avgSalesPrice;
    private BigDecimal potentialSalesAmount;

    public static ManufacturerReportResponse from(ManufacturerReportResult result) {
        return new ManufacturerReportResponse(
                result.getStatDate(),
                result.getManufacturer(),
                result.getProductCount(),
                result.getAvgSalesPrice(),
                result.getPotentialSalesAmount()
        );
    }
}
