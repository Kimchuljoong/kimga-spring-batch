package kr.co.kimga.ecommerce.api.domain.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "products")
@NoArgsConstructor
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

    public void increaseStock(int stockQuantity) {
        validateStockQuantity(stockQuantity);
        if (this.stockQuantity + stockQuantity < 0) {
            throw new StockQuantityArithmeticException();
        }

        this.stockQuantity += stockQuantity;
    }

    private static void validateStockQuantity(int stockQuantity) {
        if (stockQuantity <= 0) {
            throw new InvalidStockQuantityException();
        }
    }

    public void decreaseStock(int stockQuantity) {
        validateStockQuantity(stockQuantity);
        if (stockQuantity <= 0) {
            throw new InvalidStockQuantityException();
        }
        if (this.stockQuantity < stockQuantity) {
            throw new InsufficientStockException();
        }
        this.stockQuantity -= stockQuantity;
    }
}
