package kr.co.kimga.batch.jobconfig.product.report;

import kr.co.kimga.batch.domain.product.report.CategoryReport;
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
public class CategoryReportFlowConfiguration {

    @Bean
    public Flow categoryReportFlow(Step categoryReportStep) {
        return new FlowBuilder<SimpleFlow>("categoryReportFlow")
                .start(categoryReportStep)
                .build();
    }

    @Bean
    @StepScope
    public Step categoryReportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            StepExecutionListener listener,
            ItemReader<CategoryReport> categoryReportReader,
            ItemWriter<CategoryReport> categoryReportWriter

    ) {
        return new StepBuilder("categoryReportStep", jobRepository)
                .<CategoryReport, CategoryReport>chunk(10, transactionManager)
                .reader(categoryReportReader)
                .writer(categoryReportWriter)
                .allowStartIfComplete(true)
                .listener(listener)
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<CategoryReport> categoryReportReader(
            DataSource datasource
    ) {
        return new JdbcCursorItemReaderBuilder<CategoryReport>()
                .dataSource(datasource)
                .name("categoryReportReader")
                .sql(
                        "select category, " +
                                "count(*) product_count, " +
                                "avg(sales_price) avg_sales_price, " +
                                "max(sales_price) max_sales_price, " +
                                "min(sales_price) min_sales_price, " +
                                "sum(stock_quantity) total_stock_quantity, " +
                                "sum(sales_price * stock_quantity) potential_sales_amount " +
                                "from products " +
                                "group by category"
                )
                .beanRowMapper(CategoryReport.class)
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<CategoryReport> categoryReportWriter(
            DataSource dataSource
    ) {
        return new JdbcBatchItemWriterBuilder<CategoryReport>()
                .dataSource(dataSource)
                .sql(
                        "insert into category_reports(" +
                                "stat_date, category, product_count, avg_sales_price, max_sales_price, min_sales_price," +
                                "total_stock_quantity, potential_sales_amount) " +
                                "values( :statDate, :category, :productCount, :avgSalesPrice, :maxSalesPrice, :minSalesPrice," +
                                ":totalStockQuantity, :potentialSalesAmount )"
                )
                .beanMapped()
                .build();
    }
}
