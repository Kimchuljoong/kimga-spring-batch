package kr.co.kimga.ecommerce.api.domain.product.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ManufacturerReportRepository extends JpaRepository<ManufacturerReport, ManufacturerReportId> {

    List<ManufacturerReport> findAllByStatDate(LocalDate statDate);
}
