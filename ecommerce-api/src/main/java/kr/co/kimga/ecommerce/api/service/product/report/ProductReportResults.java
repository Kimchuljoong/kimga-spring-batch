package kr.co.kimga.ecommerce.api.service.product.report;

import kr.co.kimga.ecommerce.api.domain.product.report.BrandReport;
import kr.co.kimga.ecommerce.api.domain.product.report.CategoryReport;
import kr.co.kimga.ecommerce.api.domain.product.report.ManufacturerReport;
import kr.co.kimga.ecommerce.api.domain.product.report.ProductStatusReport;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductReportResults {

    private List<BrandReportResult> brandReports;
    private List<CategoryReportResult> categoryReports;
    private List<ManufacturerReportResult> manufacturerReports;
    private List<ProductStatusReportResult> productStatusReports;

    public static ProductReportResults of(
            List<BrandReport> brandReports, List<CategoryReport> categoryReports,
            List<ManufacturerReport> manufacturerReports, List<ProductStatusReport> productStatusReports
    ) {
        return new ProductReportResults(
                brandReports.stream()
                        .map(BrandReportResult::from)
                        .toList(),
                categoryReports.stream()
                        .map(CategoryReportResult::from)
                        .toList(),
                manufacturerReports.stream()
                        .map(ManufacturerReportResult::from)
                        .toList(),
                productStatusReports.stream()
                        .map(ProductStatusReportResult::from)
                        .toList()
        );
    }
}
