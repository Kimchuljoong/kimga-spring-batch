package kr.co.kimga.batch.jobconfig.transaction.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kimga.batch.domain.transaction.report.TransactionReport;
import kr.co.kimga.batch.domain.transaction.report.TransactionReportMapRepository;
import kr.co.kimga.batch.dto.transaction.TransactionLog;
import kr.co.kimga.batch.service.file.SplitFilePartitioner;
import kr.co.kimga.batch.service.transaction.TransactionReportAccumulator;
import kr.co.kimga.batch.util.FileUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;

@Configuration
public class TransactionReportJobConfiguration {

    @Bean
    public Job transactinoReportJob(
            JobRepository jobRepository,
            JobExecutionListener jobExecutionListener,
            Step transactionAccStep,
            Step transactionSaveStep
    ) {
        return new JobBuilder("transactionReportJob", jobRepository)
                .start(transactionAccStep)
                .next(transactionSaveStep)
                .listener(jobExecutionListener)
                .build();
    }

    @Bean
    @JobScope
    public SplitFilePartitioner splitLogFilePartitioner(
            @Value("#{stepExecutionContext['file']}") String path,
            @Value("#{jobParameters['gridSize']}") int gridSize
    ) {
        return new SplitFilePartitioner(FileUtils.splitLog(new File(path), gridSize));
    }

    @Bean
    @JobScope
    public TaskExecutorPartitionHandler logFilePartitionHandler(
            TaskExecutor taskExecutor,
            Step transactionAccStep,
            @Value("#{jobParameters['gridSize']}") int gridSize
    ) {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setTaskExecutor(taskExecutor);
        handler.setStep(transactionAccStep);
        handler.setGridSize(gridSize);
        return handler;
    }

    @Bean
    public Step transactionAccStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            StepExecutionListener stepExecutionListener,
            ItemReader<TransactionLog> logReader,
            ItemWriter<TransactionLog> logAccumulator,
            TaskExecutor taskExecutor
    ) {
        return new StepBuilder("transactionAccStep", jobRepository)
                .<TransactionLog, TransactionLog>chunk(1000, transactionManager)
                .reader(logReader)
                .writer(logAccumulator)
                .allowStartIfComplete(true)
                .listener(stepExecutionListener)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamReader<TransactionLog> logReader(
            @Value("#{jobParameters['inputFilePath']}") String path,
            ObjectMapper objectMapper
    ) {
        FlatFileItemReader<TransactionLog> logReader = new FlatFileItemReaderBuilder<TransactionLog>().name("logReader")
                .resource(new FileSystemResource(path))
                .lineMapper(
                        (line, lineNumber) ->
                                objectMapper.readValue(line, TransactionLog.class))
                .build();
        return new SynchronizedItemStreamReaderBuilder<TransactionLog>()
                .delegate(logReader)
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<TransactionLog> logAccumulator(TransactionReportAccumulator accumulator) {
        return chunk -> {
            for (TransactionLog transactionLog : chunk.getItems()) {
                accumulator.accumulate(transactionLog);
            }
        };
    }

    @Bean
    public Step transactionSaveStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            StepExecutionListener stepExecutionListener,
            ItemReader<TransactionReport> reportReader,
            ItemWriter<TransactionReport> reportWriter
    ) {
        return new StepBuilder("transactionSaveStep", jobRepository)
                .<TransactionReport, TransactionReport>chunk(10, transactionManager)
                .reader(reportReader)
                .writer(reportWriter)
                .allowStartIfComplete(true)
                .listener(stepExecutionListener)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<TransactionReport> reportReader(TransactionReportMapRepository repository) {
        return new IteratorItemReader<>(repository.getTransactionReports());
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<TransactionReport> reportWriter(DataSource dataSource) {
        String sql = "insert into transaction_reports(transaction_date, transaction_type, transaction_count, " +
                "total_amount, customer_count, order_count, payment_method_count, avg_product_count, total_item_quantity) " +
                "values(:transactionDate, :transactionType, :transactionCount, :totalAmount, :customerCount, " +
                ":paymentMethodCount, :avgProductCount, :totalItemQuantity)";
        return new JdbcBatchItemWriterBuilder<TransactionReport>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }
}
