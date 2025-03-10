package kr.co.kimga.batch.jobconfig.product.upload;

import kr.co.kimga.batch.jobconfig.BaseBatchIntegrationTest;
import kr.co.kimga.batch.service.product.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestPropertySource(properties = {"spring.batch.job.name=productUploadJob"})
class ProductUploadJobConfigurationTest extends BaseBatchIntegrationTest {

    @Value("classpath:/data/products_for_upload.csv")
    private Resource input;

    @Autowired
    private ProductService productService;

    @Test
    void testJob(@Autowired Job productUploadJob) throws Exception {
        JobParameters jobParameters = jobParameters();
        jobLauncherTestUtils.setJob(productUploadJob);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertAll(
                () -> assertThat(productService.countProducts()).isEqualTo(6),
                () -> assertJobCompleted(jobExecution));
    }


    private JobParameters jobParameters() throws IOException {
        return new JobParametersBuilder()
                .addJobParameter("inputFilePath", new JobParameter<>(input.getFile().getPath(), String.class, false))
                .addJobParameter("gridSize", new JobParameter<>(3, Integer.class, false))
                .toJobParameters();
    }
}