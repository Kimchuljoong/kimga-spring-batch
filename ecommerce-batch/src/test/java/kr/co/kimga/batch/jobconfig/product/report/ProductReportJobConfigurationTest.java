package kr.co.kimga.batch.jobconfig.product.report;

import kr.co.kimga.batch.domain.product.Product;
import kr.co.kimga.batch.jobconfig.BaseBatchIntegrationTest;
import kr.co.kimga.batch.service.product.ProductService;
import kr.co.kimga.batch.service.product.report.ProductReportService;
import kr.co.kimga.batch.util.DateTimeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestPropertySource(properties = {"spring.batch.job.name=productReportJob"})
class ProductReportJobConfigurationTest extends BaseBatchIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductReportService productReportService;

    @Test
    public void testJob(
            @Autowired Job productReportJob
    ) throws Exception {
        LocalDate now = LocalDate.now();

        saveProduct();
        jobLauncherTestUtils.setJob(productReportJob);
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertAll(
                () -> assertThat(productReportService.countCategoryReport(now)).isEqualTo(1),
                () -> assertThat(productReportService.countBrandReport(now)).isEqualTo(2),
                () -> assertThat(productReportService.countManufacturerReport(now)).isEqualTo(2),
                () -> assertThat(productReportService.countProductStatusReport(now)).isEqualTo(1),
                () -> assertJobCompleted(jobExecution)
        );
    }

    private void saveProduct() {
        productService.save(Product.of("1", 1L, "서적", "소파", LocalDate.of(2025, 5, 21),
                LocalDate.of(2025, 12, 6), "AVAILABLE", "삼성", "아모레퍼시픽", 333152, 557,
                DateTimeUtils.toLocalDateTime("2025-02-22 14:44:21.112"),
                DateTimeUtils.toLocalDateTime("2025-02-22 14:44:21.112")));
        productService.save(Product.of("2", 2L, "가구", "TV", LocalDate.of(2020, 11, 10),
                LocalDate.of(2025, 1, 17), "DISCONTINUED", "맥도날드", "스타벅스코리아", 176120, 878,
                DateTimeUtils.toLocalDateTime("2025-02-23 11:45:21.112"),
                DateTimeUtils.toLocalDateTime("2025-02-23 11:45:21.112")));

    }

}