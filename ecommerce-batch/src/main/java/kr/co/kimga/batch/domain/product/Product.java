package kr.co.kimga.batch.domain.product;

import kr.co.kimga.batch.dto.ProductUploadCsvRow;
import kr.co.kimga.batch.util.DateTimeUtils;
import kr.co.kimga.batch.util.RandomUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {
    private String productId;
    private Long sellerId;

    private String category;
    private String productName;
    private LocalDate salesStartDate;
    private LocalDate salesEndDate;
    private String productStatus;
    private String brand;
    private String manufacturer;

    private int salesPrice;
    private int stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Product from(ProductUploadCsvRow row) {
        LocalDateTime now = LocalDateTime.now();

        return new Product(
                RandomUtils.generateRandomId(),
                row.getSellerId(),
                row.getCategory(),
                row.getProductName(),
                DateTimeUtils.toLocalDate(row.getSalesStartDate()),
                DateTimeUtils.toLocalDate(row.getSalesEndDate()),
                row.getProductStatus(),
                row.getBrand(),
                row.getManufacturer(),
                row.getSalesPrice(),
                row.getStockQuantity(),
                now,
                now
        );
    }
}
