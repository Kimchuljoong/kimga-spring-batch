package kr.co.kimga.batch.jobconfig.product.download;

import kr.co.kimga.batch.domain.product.Product;
import kr.co.kimga.batch.jobconfig.BaseBatchIntegrationTest;
import kr.co.kimga.batch.service.product.ProductService;
import kr.co.kimga.batch.util.DateTimeUtils;
import kr.co.kimga.batch.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestPropertySource(properties = {"spring.batch.job.name=productDownloadJob"})
class ProductDownloadJobConfigurationTest extends BaseBatchIntegrationTest {

    @Value("classpath:/data/products_downloaded_expected.csv")
    private Resource expectedResource;
    private File outPutFile;

    @Autowired
    private ProductService productService;

    @Test
    void testJob(@Autowired Job productDownloadJob) throws Exception {
        saveProduct();
        outPutFile = FileUtils.createTempFile("products_downloaded", ".csv");
        JobParameters jobParameters = jobParameters();
        jobLauncherTestUtils.setJob(productDownloadJob);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertAll(
                () -> assertThat(
                        Files.readString(Path.of(outPutFile.getPath()))).isEqualTo(
                        Files.readString(Path.of(expectedResource.getFile().getPath()))),
                () -> assertJobCompleted(jobExecution));
    }


    private JobParameters jobParameters() throws IOException {
        return new JobParametersBuilder()
                .addJobParameter("outputFilePath",
                        new JobParameter<>(outPutFile.getPath(), String.class, false))
                .toJobParameters();
    }

    private void saveProduct() {
        /*
        1,1,서적,소파,2020-05-21,2025-12-06,AVAILABLE,삼성,아모레퍼시픽,333152,557,2025-02-22 14:44:21.112,2025-02-22 14:44:21.112
2,2,가구,TV,2020-11-10,2025-01-17,DISCONTINUED,맥도날드,스타벅스코리아,176120,878,2025-02-23 11:45:21.112,2025-02-23 11:45:21.112
        */
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
