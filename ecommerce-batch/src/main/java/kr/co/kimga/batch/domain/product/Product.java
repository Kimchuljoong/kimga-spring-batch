package kr.co.kimga.batch.domain.product;

import jakarta.persistence.*;
import kr.co.kimga.batch.dto.upload.ProductUploadCsvRow;
import kr.co.kimga.batch.util.DateTimeUtils;
import kr.co.kimga.batch.util.RandomUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {

    @Id
    private String productId;
    private Long sellerId;

    private String category;
    private String productName;
    private LocalDate salesStartDate;
    private LocalDate salesEndDate;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;
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
                ProductStatus.valueOf(row.getProductStatus()),
                row.getBrand(),
                row.getManufacturer(),
                row.getSalesPrice(),
                row.getStockQuantity(),
                now,
                now
        );
    }

    public static Product of(
            String productId, Long sellerId, String category,
            String productName, LocalDate salesStartDate, LocalDate salesEndDate,
            ProductStatus productStatus, String brand, String manufacturer,
            int salesPrice, int stockQuantity, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return new Product(productId, sellerId, category, productName
                , salesStartDate, salesEndDate, productStatus, brand
                , manufacturer, salesPrice, stockQuantity, createdAt
                , updatedAt);
    }
}
