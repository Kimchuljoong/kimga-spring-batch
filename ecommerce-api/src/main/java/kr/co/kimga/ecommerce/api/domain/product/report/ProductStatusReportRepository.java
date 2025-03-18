package kr.co.kimga.ecommerce.api.domain.product.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductStatusReportRepository extends JpaRepository<ProductStatusReport, ProductStatusReportId> {

    List<ProductStatusReport> findAllByStatDate(LocalDate statDate);
}
