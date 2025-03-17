package kr.co.kimga.batch.jobconfig.product.report;

import kr.co.kimga.batch.domain.product.report.ProductStatusReport;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class ProductStatusReportConfiguration {

    @Bean
    public Flow productStatusReportFlow(Step productStatusReportStep) {
        return new FlowBuilder<SimpleFlow>("productStatusReportFlow")
                .start(productStatusReportStep)
                .build();
    }

    @Bean
    @StepScope
    public Step productStatusReportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            StepExecutionListener listener,
            ItemReader<ProductStatusReport> productStatusReportReader,
            ItemWriter<ProductStatusReport> productStatusReportWriter
    ) {
        return new StepBuilder("productStatusReportStep", jobRepository)
                .<ProductStatusReport, ProductStatusReport>chunk(10, transactionManager)
                .reader(productStatusReportReader)
                .writer(productStatusReportWriter)
                .allowStartIfComplete(true)
                .listener(listener)
                .build();
    }


    @Bean
    @StepScope
    public JdbcCursorItemReader<ProductStatusReport> productStatusReportReader(
            DataSource datasource
    ) {
        return new JdbcCursorItemReaderBuilder<ProductStatusReport>()
                .dataSource(datasource)
                .name("productStatusReportReader")
                .sql(
                        "select product_status, " +
                                "count(*) product_count, " +
                                "avg(sales_price) avg_sales_price " +
                                "from products " +
                                "group by product_status"
                )
                .beanRowMapper(ProductStatusReport.class)
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<ProductStatusReport> productStatusReportWriter(
            DataSource dataSource
    ) {
        return new JdbcBatchItemWriterBuilder<ProductStatusReport>()
                .dataSource(dataSource)
                .sql(
                        "insert into product_status_reports(" +
                                "stat_date, product_status, product_count, avg_sales_price) " +
                                "values( :statDate, :productStatus, :productCount, :avgSalesPrice)"
                )
                .beanMapped()
                .build();
    }
}
