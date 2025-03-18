package kr.co.kimga.ecommerce.api.service.product.report;

import kr.co.kimga.ecommerce.api.domain.product.report.BrandReportRepository;
import kr.co.kimga.ecommerce.api.domain.product.report.CategoryReportRepository;
import kr.co.kimga.ecommerce.api.domain.product.report.ManufacturerReportRepository;
import kr.co.kimga.ecommerce.api.domain.product.report.ProductStatusReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ProductReportService {

    private final BrandReportRepository brandReportRepository;
    private final CategoryReportRepository categoryReportRepository;
    private final ManufacturerReportRepository manufacturerReportRepository;
    private final ProductStatusReportRepository productStatusReportRepository;

    public ProductReportResults findReports(LocalDate date) {
        return ProductReportResults.of(
                brandReportRepository.findAllByStatDate(date),
                categoryReportRepository.findAllByStatDate(date),
                manufacturerReportRepository.findAllByStatDate(date),
                productStatusReportRepository.findAllByStatDate(date)
        );
    }
}
