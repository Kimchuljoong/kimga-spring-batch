package kr.co.kimga.batch.dto.download;

import kr.co.kimga.batch.domain.product.Product;
import kr.co.kimga.batch.util.DateTimeUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDownloadCsvRow {

    private String productId;
    private Long sellerId;

    private String category;
    private String productName;
    private String salesStartDate;
    private String salesEndDate;
    private String productStatus;
    private String brand;
    private String manufacturer;

    private int salesPrice;
    private int stockQuantity;
    private String createdAt;
    private String updatedAt;

    public static ProductDownloadCsvRow from(Product product) {
        return new ProductDownloadCsvRow(
                product.getProductId(),
                product.getSellerId(),
                product.getCategory(),
                product.getProductName(),
                product.getSalesStartDate().toString(),
                product.getSalesEndDate().toString(),
                product.getProductStatus(),
                product.getBrand(),
                product.getBrand(),
                product.getSalesPrice(),
                product.getStockQuantity(),
                DateTimeUtils.toString(product.getCreatedAt()),
                DateTimeUtils.toString(product.getUpdatedAt())
        );
    }
}
