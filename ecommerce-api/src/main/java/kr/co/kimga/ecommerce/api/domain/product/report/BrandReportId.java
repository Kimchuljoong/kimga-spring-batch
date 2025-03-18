package kr.co.kimga.ecommerce.api.domain.product.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandReportId implements Serializable {

    private LocalDate statDate;
    private String brand;
}
