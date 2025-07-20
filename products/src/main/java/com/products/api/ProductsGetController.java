package com.products.api;

import com.products.products.domain.ProductRepository;
import com.products.products.domain.ProductResponse;
import com.products.products.domain.ProductSearchCriteria;
import com.products.services.products.ProductsFinder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class ProductsGetController {

    private final ProductsFinder productsFinder;

    @GetMapping("/api/v1/products/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> getProductById(@PathVariable UUID id) {
        return productsFinder.findById(id)
                .map(optionalProduct -> optionalProduct
                        .<ResponseEntity<Map<String, Object>>>map(productResponse ->
                                ResponseEntity.ok(Map.of("data", productResponse))
                        )
                        .orElseGet(() ->
                                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("data", null))
                        )
                );
    }

    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID brandId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        ProductSearchCriteria criteria = new ProductSearchCriteria(name, brandId, categoryId, minPrice, maxPrice);

        return productsFinder.search(criteria)
                .collectList()
                .map(products -> {
                    if (products.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("data", List.of(), "message", "No products found"));
                    } else {
                        return ResponseEntity.ok(Map.of("data", products));
                    }
                });
    }
}
