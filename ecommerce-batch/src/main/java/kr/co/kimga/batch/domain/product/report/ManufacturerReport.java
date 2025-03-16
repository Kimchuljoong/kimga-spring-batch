package kr.co.kimga.batch.domain.product.report;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ManufacturerReport {

    private LocalDate statDate = LocalDate.now();

    private String manufacturer;
    private Integer productCount;
    private BigDecimal avgSalesPrice;
    private Integer potentialSalesAmount;
}
