package kr.co.kimga.batch.jobconfig.product.report;

import kr.co.kimga.batch.domain.product.report.BrandReport;
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
public class BrandReportFlowConfiguration {

    @Bean
    public Flow brandReportFlow(Step brandReportStep) {
        return new FlowBuilder<SimpleFlow>("brandReportFlow")
                .start(brandReportStep)
                .build();
    }

    @Bean
    public Step brandReportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            StepExecutionListener listener,
            ItemReader<BrandReport> brandReportReader,
            ItemWriter<BrandReport> brandReportWriter

    ) {
        return new StepBuilder("brandReportStep", jobRepository)
                .<BrandReport, BrandReport>chunk(10, transactionManager)
                .reader(brandReportReader)
                .writer(brandReportWriter)
                .allowStartIfComplete(true)
                .listener(listener)
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<BrandReport> brandReportReader(
            DataSource datasource
    ) {
        return new JdbcCursorItemReaderBuilder<BrandReport>()
                .dataSource(datasource)
                .name("brandReportReader")
                .sql(
                        "select brand, " +
                                "count(*) product_count, " +
                                "avg(sales_price) avg_sales_price, " +
                                "max(sales_price) max_sales_price, " +
                                "min(sales_price) min_sales_price, " +
                                "sum(stock_quantity) total_stock_quantity, " +
                                "avg(stock_quantity) avg_stock_quantity, " +
                                "sum(sales_price * stock_quantity) potential_sales_amount " +
                                "from products " +
                                "group by brand"
                )
                .beanRowMapper(BrandReport.class)
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<BrandReport> brandReportWriter(
            DataSource dataSource
    ) {
        return new JdbcBatchItemWriterBuilder<BrandReport>()
                .dataSource(dataSource)
                .sql(
                        "insert into brand_reports(" +
                                "stat_date, brand, product_count, avg_sales_price, max_sales_price, min_sales_price," +
                                "total_stock_quantity, avg_stock_quantity, potential_sales_amount) " +
                                "values( :statDate, :brand, :productCount, :avgSalesPrice, :maxSalesPrice, :minSalesPrice," +
                                ":totalStockQuantity, :avgStockQuantity, :potentialSalesAmount )"
                )
                .beanMapped()
                .build();
    }
}
