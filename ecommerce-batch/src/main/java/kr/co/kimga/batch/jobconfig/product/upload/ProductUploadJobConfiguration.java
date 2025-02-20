package kr.co.kimga.batch.jobconfig.product.upload;

import kr.co.kimga.batch.domain.product.Product;
import kr.co.kimga.batch.dto.ProductUploadCsvRow;
import kr.co.kimga.batch.service.file.SplitFilePartitioner;
import kr.co.kimga.batch.util.FileUtils;
import kr.co.kimga.batch.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemReader;
import org.springframework.batch.item.support.builder.SynchronizedItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;

@Slf4j
@Configuration
public class ProductUploadJobConfiguration {

    @Bean
    public Job productUploadJob(JobRepository jobRepository,
                                Step productUploadStep,
                                JobExecutionListener jobExecutionListener) {
        return new JobBuilder("productUploadJob", jobRepository)
                .listener(jobExecutionListener)
                .start(productUploadStep)
                .build();
    }

    @Bean
    public Step productUploadPartitionStep(
            JobRepository jobRepository,
            Step productUploadStep,
            SplitFilePartitioner splitFilePartitioner
    ) {
        return new StepBuilder("productUploadPartitionStep", jobRepository)
                .partitioner(productUploadStep.getName(), splitFilePartitioner)
                .partitionHandler(filePartitionHandler)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    @JobScope
    public SplitFilePartitioner splitFilePartitioner(
            @Value("#{jobParameters['inputFilePath']}") String path,
            @Value("#{jobParameters['gridSize']}") int gridSize
    ) {
        return new SplitFilePartitioner(FileUtils.splitCsv(new File(path)), gridSize);
    }

    @Bean
    public TaskExecutorPartitionHandler filePartitionHandler(TaskExecutor taskExecutor) {
        

    }

    @Bean
    public Step productUploadStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  StepExecutionListener stepExecutionListener,
                                  ItemReader<ProductUploadCsvRow> productReader,
                                  ItemProcessor<ProductUploadCsvRow, Product> productProcessor,
                                  ItemWriter<Product> productWriter,
                                  @Qualifier("customTaskExecutor") TaskExecutor taskExecutor
    ) {
        return new StepBuilder("productUploadStep", jobRepository)
                .<ProductUploadCsvRow, Product>chunk(1000, transactionManager)
                .reader(productReader)
                .processor(productProcessor)
                .writer(productWriter)
                .allowStartIfComplete(true)
                .listener(stepExecutionListener)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    @StepScope
    public SynchronizedItemReader<ProductUploadCsvRow> productReader(
            @Value("#{jobParameters['inputFilePath']}") String path
    ) {
        FlatFileItemReader<ProductUploadCsvRow> productReader = new FlatFileItemReaderBuilder<ProductUploadCsvRow>()
                .name("productReader")
                .resource(new FileSystemResource(path))
                .delimited()
                .names(ReflectionUtils.getFieldNames(ProductUploadCsvRow.class).toArray(String[]::new))
                .targetType(ProductUploadCsvRow.class)
                .linesToSkip(1)
                .build();
        return new SynchronizedItemReaderBuilder<ProductUploadCsvRow>()
                .delegate(productReader)
                .build();
    }

    @Bean
    public ItemProcessor<ProductUploadCsvRow, Product> productProcessor() {
        return Product::from;
    }

    @Bean
    public ItemWriter<Product> productWriter(DataSource dataSource) {
        String sql = "insert into products(product_id, seller_id, category, product_name," +
                "sales_start_date, sales_end_date, product_status, brand, manufacturer, sales_price," +
                "stock_quantity, created_at, updated_at) values(:productId, :sellerId, :category, :productName," +
                ":salesStartDate, :salesEndDate, :productStatus, :brand, :manufacturer, :salesPrice," +
                ":stockQuantity, :createdAt, :updatedAt)";

        return new JdbcBatchItemWriterBuilder<Product>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }
}
