package kr.co.kimga.batch.service.product;

import kr.co.kimga.batch.domain.product.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final JdbcTemplate jdbcTemplate;

    public Long countProducts() {
        return jdbcTemplate.queryForObject("select count(*) from products", Long.class);
    }

    public void save(Product product) {
        String sql = "insert into products(product_id, seller_id, category, product_name, " +
                "sales_start_date, sales_end_date, product_status, brand, manufacturer, " +
                "sales_price, stock_quantity, created_at, updated_at) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
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
