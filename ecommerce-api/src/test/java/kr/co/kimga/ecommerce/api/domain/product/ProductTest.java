package kr.co.kimga.ecommerce.api.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {

        LocalDateTime now = LocalDateTime.now();

        product = Product.of(
                "PRODUCT-1", 1L, "Electronics", "TestProduct",
                now.toLocalDate(), now.toLocalDate(), ProductStatus.AVAILABLE, "Test Brand",
                "Test Manufacturer", 1000, 100, now, now);
    }

    @Test
    void testIncreaseStock() {
        product.increaseStock(50);

        Assertions.assertThat(product.getStockQuantity()).isEqualTo(150);
    }

    @Test
    void testIncreaseStockNegativeResult() {
        Assertions.assertThatThrownBy(() -> product.increaseStock(Integer.MAX_VALUE))
                .isInstanceOf(StockQuantityArithmeticException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, -1, 0})
    void testIncreaseStockPositiveParameter(int notPositiveQuantity) {
        Assertions.assertThatThrownBy(() -> product.increaseStock(notPositiveQuantity))
                .isInstanceOf(InvalidStockQuantityException.class);
    }

    @Test
    void testDecreaseStock() {
        product.decreaseStock(50);

        Assertions.assertThat(product.getStockQuantity()).isEqualTo(50);
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, -1, 0})
    void testDecreaseStockPositiveParameter(int notPositiveQuantity) {
        Assertions.assertThatThrownBy(() -> product.decreaseStock(notPositiveQuantity))
                .isInstanceOf(InvalidStockQuantityException.class);
    }

    @Test
    void testDecreaseStockWithInsufficientStock() {
        Assertions.assertThatThrownBy(() -> product.decreaseStock(101))
                .isInstanceOf(InsufficientStockException.class);
    }
}