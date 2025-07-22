package com.products.product.services;

import com.products.product.domain.Product;
import com.products.product.domain.ProductRepository;
import com.products.product.domain.ProductResponse;
import com.products.product.domain.ProductSearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductsFinder {

    private final ProductRepository productRepository;

    public Mono<Optional<ProductResponse>> findById(UUID id) {
        return productRepository.FindById(id)
                .map(optProduct -> optProduct.map(this::toResponse));
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getBrandId(),
                product.getCategoryId()
        );
    }

    public Flux<ProductResponse> search(ProductSearchCriteria criteria) {
        return productRepository.search(criteria)
                .map(this::toResponse);
    }
}