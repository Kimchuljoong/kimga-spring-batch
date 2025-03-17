package kr.co.kimga.batch.service.product.report;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ProductReportService {

    private final JdbcTemplate jdbcTemplate;

    public Long countProductStatusReport(LocalDate now) {
        return jdbcTemplate.queryForObject(
                "select count(*) from product_status_reports where stat_date='" + now + "'",
                Long.class);
    }

    public Long countManufacturerReport(LocalDate now) {
        return jdbcTemplate.queryForObject(
                "select count(*) from manufacturer_reports where stat_date='" + now + "'",
                Long.class);
    }

    public Long countBrandReport(LocalDate now) {
        return jdbcTemplate.queryForObject(
                "select count(*) from brand_reports where stat_date='" + now + "'",
                Long.class);
    }

    public Long countCategoryReport(LocalDate now) {
        return jdbcTemplate.queryForObject(
                "select count(*) from category_reports where stat_date='" + now + "'",
                Long.class);
    }

}
