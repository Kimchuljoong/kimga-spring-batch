package kr.co.kimga.batch.service.product;

import kr.co.kimga.batch.domain.product.Product;
import kr.co.kimga.batch.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final JdbcTemplate jdbcTemplate;
    private final ProductRepository productRepository;

    public Long countProducts() {
        return productRepository.count();
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public List<String> getProductIds() {
        return productRepository.findAllProjectIds();
    }

}
