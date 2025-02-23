package kr.co.kimga.batch.jobconfig.product.download;

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
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
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
            ItemWriter<ProductDownloadCsvRow> productCsvWriter
    ) {
        return new StepBuilder("productPagingStep", jobRepository)
                .<Product, ProductDownloadCsvRow>chunk(1000, transactionManager)
                .reader(productPagingReader)
                .processor(productDownloadProcessor)
                .writer(productCsvWriter)
                .listener(stepExecutionListener)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Product> productPagingReader(
            DataSource dataSource,
            PagingQueryProvider productPagingQueryProvider
    ) {
        return new JdbcPagingItemReaderBuilder<Product>()
                .dataSource(dataSource)
                .name("productPagingReader")
                .queryProvider(productPagingQueryProvider)
                .pageSize(1000)
                .beanRowMapper(Product.class)
                .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean productPagingQueryProvider(DataSource dataSource) {
        SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
        provider.setDataSource(dataSource);
        provider.setSelectClause(
                "select product_id, seller_id, category, product_name, " +
                        "sales_start_date, sales_end_date, product_status, brand, manufacturer, " +
                        "sales_price, stock_quantity, created_at, updated_at");
        provider.setFromClause("from products");
        provider.setSortKey("product_id");
        return provider;
    }

    @Bean
    public ItemProcessor<Product, ProductDownloadCsvRow> productDownloadProcessor() {
        return ProductDownloadCsvRow::from;
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Product> productCsvWriter(
            @Value("#{jobParameters['outputFilePath']}") String path
    ) {
        List<String> columns = ReflectionUtils.getFieldNames(ProductDownloadCsvRow.class);
        return new FlatFileItemWriterBuilder<Product>()
                .name("productCsvWriter")
                .resource(new FileSystemResource(path))
                .delimited()
                .names(columns.toArray(String[]::new))
                .headerCallback(writer -> writer.write(String.join(",", columns)))
                .build();
    }
}
