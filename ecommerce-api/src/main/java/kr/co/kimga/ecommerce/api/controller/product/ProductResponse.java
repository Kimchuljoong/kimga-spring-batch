package kr.co.kimga.ecommerce.api.controller.product;

import kr.co.kimga.ecommerce.api.domain.product.ProductStatus;
import kr.co.kimga.ecommerce.api.service.product.ProductResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductResponse {

    private String productId;
    private Long sellerId;

    private String category;
    private String productName;
    private LocalDate salesStartDate;
    private LocalDate salesEndDate;

    private ProductStatus productStatus;
    private String brand;
    private String manufacturer;

    private int salesPrice;
    private int stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductResponse of(
            String productId, Long sellerId, String category,
            String productName, LocalDate salesStartDate, LocalDate salesEndDate,
            ProductStatus productStatus, String brand, String manufacturer,
            int salesPrice, int stockQuantity, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return new ProductResponse(productId, sellerId, category, productName
                , salesStartDate, salesEndDate, productStatus, brand
                , manufacturer, salesPrice, stockQuantity, createdAt
                , updatedAt);
    }

    public static ProductResponse from(ProductResult product) {
        return new ProductResponse(
                product.getProductId(),
                product.getSellerId(),
                product.getCategory(),
                product.getProductName(),
                product.getSalesStartDate(),
                product.getSalesEndDate(),
                product.getProductStatus(),
                product.getBrand(),
                product.getManufacturer(),
                product.getSalesPrice(),
                product.getStockQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
