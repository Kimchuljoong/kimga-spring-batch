package kr.co.kimga.batch.jobconfig.transaction.report;

import kr.co.kimga.batch.jobconfig.BaseBatchIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestPropertySource(properties = {"spring.batch.job.name=transactionReportJob"})
class TransactionReportJobConfigurationTest extends BaseBatchIntegrationTest {

    @Value("classpath:/logs/transaction.log")
    private Resource resource;

    @Test
    public void testJob(@Autowired Job transactionReportJob) throws Exception {
        jobLauncherTestUtils.setJob(transactionReportJob);
        JobParameters jobParameter = getJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameter);
        assertAll(
                () -> assertThat(
                        jdbcTemplate.queryForObject("select count(*) from transaction_reports", Integer.class)
                ).isEqualTo(3),
                () -> assertJobCompleted(jobExecution)
        );

    }

    private JobParameters getJobParameters() throws IOException {
        return new JobParametersBuilder()
                .addJobParameter("inputFilePath",
                        new JobParameter<>(resource.getFile().getPath(), String.class, false))
                .toJobParameters();
    }

}