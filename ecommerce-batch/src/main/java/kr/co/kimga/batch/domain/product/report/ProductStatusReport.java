package kr.co.kimga.batch.domain.product.report;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ProductStatusReport {

    private LocalDate statDate = LocalDate.now();

    private String productStatus;
    private Long productCount;
    private BigDecimal avgStockQuantity;
}
