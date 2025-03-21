package kr.co.kimga.batch.jobconfig.product.download;

import jakarta.persistence.EntityManagerFactory;
import kr.co.kimga.batch.domain.product.Product;
import kr.co.kimga.batch.dto.download.ProductDownloadCsvRow;
import kr.co.kimga.batch.util.ReflectionUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.SynchronizedItemWriter;
import org.springframework.batch.item.support.builder.SynchronizedItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
public class ProductDownloadJobConfiguration {

    @Bean
    public Job productDownloadJob(
            JobRepository jobRepository,
            Step productPagingStep,
            JobExecutionListener jobExecutionListener
    ) {
        return new JobBuilder("productDownloadJob", jobRepository)
                .start(productPagingStep)
                .listener(jobExecutionListener)
                .build();
    }

    @Bean
    public Step productPagingStep(
            JobRepository jobRepository,
            StepExecutionListener stepExecutionListener,
            PlatformTransactionManager transactionManager,
            ItemReader<Product> productPagingReader,
            ItemProcessor<Product, ProductDownloadCsvRow> productDownloadProcessor,
            ItemWriter<ProductDownloadCsvRow> productCsvWriter,
            TaskExecutor taskExecutor
    ) {
        return new StepBuilder("productPagingStep", jobRepository)
                .<Product, ProductDownloadCsvRow>chunk(1000, transactionManager)
                .reader(productPagingReader)
                .processor(productDownloadProcessor)
                .writer(productCsvWriter)
                .allowStartIfComplete(true)
                .listener(stepExecutionListener)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Product> productPagingReader(
            EntityManagerFactory entityManagerFactory
    ) {
        return new JpaPagingItemReaderBuilder<Product>()
                .entityManagerFactory(entityManagerFactory)
                .name("productPagingReader")
                .queryString("select p from Product p")
                .pageSize(1000)
                .build();
    }

    @Bean
    public ItemProcessor<Product, ProductDownloadCsvRow> productDownloadProcessor() {
        return ProductDownloadCsvRow::from;
    }

    @Bean
    @StepScope
    public SynchronizedItemWriter<ProductDownloadCsvRow> productCsvWriter(
            @Value("#{jobParameters['outputFilePath']}") String path
    ) {
        List<String> columns = ReflectionUtils.getFieldNames(ProductDownloadCsvRow.class);
        FlatFileItemWriter<ProductDownloadCsvRow> productCsvWriter = new FlatFileItemWriterBuilder<ProductDownloadCsvRow>()
                .name("productCsvWriter")
                .resource(new FileSystemResource(path))
                .delimited()
                .names(columns.toArray(String[]::new))
                .headerCallback(writer -> writer.write(String.join(",", columns)))
                .build();

        return new SynchronizedItemWriterBuilder<ProductDownloadCsvRow>()
                .delegate(productCsvWriter)
                .build();
    }
}
