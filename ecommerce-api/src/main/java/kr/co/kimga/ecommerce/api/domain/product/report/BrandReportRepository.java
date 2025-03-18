package kr.co.kimga.ecommerce.api.domain.product.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BrandReportRepository extends JpaRepository<BrandReport, BrandReportId> {

    List<BrandReport> findAllByStatDate(LocalDate statDate);
}
