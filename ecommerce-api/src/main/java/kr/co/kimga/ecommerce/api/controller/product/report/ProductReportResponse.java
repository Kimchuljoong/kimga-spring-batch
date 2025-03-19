package kr.co.kimga.ecommerce.api.controller.product.report;

import kr.co.kimga.ecommerce.api.service.product.report.ProductReportResults;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductReportResponse {

    private List<BrandReportResponse> brandReports;
    private List<CategoryReportResponse> categoryReports;
    private List<ManufacturerReportResponse> manufacturerReports;
    private List<ProductStatusReportResponse> productStatusReports;

    public static ProductReportResponse from(ProductReportResults results) {
        return new ProductReportResponse(
                results.getBrandReports().stream()
                        .map(BrandReportResponse::from)
                        .toList(),
                results.getCategoryReports().stream()
                        .map(CategoryReportResponse::from)
                        .toList(),
                results.getManufacturerReports().stream()
                        .map(ManufacturerReportResponse::from)
                        .toList(),
                results.getProductStatusReports().stream()
                        .map(ProductStatusReportResponse::from)
                        .toList()
        );
    }
}
