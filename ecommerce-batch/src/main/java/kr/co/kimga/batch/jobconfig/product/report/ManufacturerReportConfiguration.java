package kr.co.kimga.batch.jobconfig.product.report;

import kr.co.kimga.batch.domain.product.report.ManufacturerReport;
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
public class ManufacturerReportConfiguration {

    @Bean
    public Flow manufacturerReportFlow(Step manufacturerReportStep) {
        return new FlowBuilder<SimpleFlow>("manufacturerReportFlow")
                .start(manufacturerReportStep)
                .build();
    }

    @Bean
    @StepScope
    public Step manufacturerReportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            StepExecutionListener listener,
            ItemReader<ManufacturerReport> manufacturerReportReader,
            ItemWriter<ManufacturerReport> manufacturerReportWriter
    ) {
        return new StepBuilder("manufacturerReportStep", jobRepository)
                .<ManufacturerReport, ManufacturerReport>chunk(10, transactionManager)
                .reader(manufacturerReportReader)
                .writer(manufacturerReportWriter)
                .allowStartIfComplete(true)
                .listener(listener)
                .build();
    }


    @Bean
    @StepScope
    public JdbcCursorItemReader<ManufacturerReport> manufacturerReportReader(
            DataSource datasource
    ) {
        return new JdbcCursorItemReaderBuilder<ManufacturerReport>()
                .dataSource(datasource)
                .name("manufacturerReportReader")
                .sql(
                        "select manufacturer, " +
                                "count(*) product_count, " +
                                "avg(sales_price) avg_sales_price, " +
                                "sum(sales_price * stock_quantity) potential_sales_amount " +
                                "from products " +
                                "group by manufacturer"
                )
                .beanRowMapper(ManufacturerReport.class)
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<ManufacturerReport> manufacturerReportWriter(
            DataSource dataSource
    ) {
        return new JdbcBatchItemWriterBuilder<ManufacturerReport>()
                .dataSource(dataSource)
                .sql(
                        "insert into manufacturer_reports(" +
                                "stat_date, manufacturer, product_count, avg_sales_price, potential_sales_amount) " +
                                "values( :statDate, :manufacturer, :productCount, :avgSalesPrice, :potentialSalesAmount )"
                )
                .beanMapped()
                .build();
    }
}
