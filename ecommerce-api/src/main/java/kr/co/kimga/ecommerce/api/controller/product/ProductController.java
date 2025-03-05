package kr.co.kimga.ecommerce.api.controller.product;

import kr.co.kimga.ecommerce.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/v1/products")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("{productId}")
    public ProductResponse getProduct(
            @PathVariable String productId
    ) {
        return ProductResponse.from(productService.findProduct(productId));
    }

    @GetMapping("")
    private Page<ProductResponse> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productId,asc") String[] sort
    ) {
        Sort.Direction sortDirection =
                sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return productService.getAllProducts(
                PageRequest.of(page, size, Sort.by(sortDirection, sort[0]))
        ).map(ProductResponse::from);
    }
}
