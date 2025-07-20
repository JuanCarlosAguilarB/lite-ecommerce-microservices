package com.products.products.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Mono<Void> save(Product product);

    Mono<Optional<Product>> FindById(UUID id);
    Flux<Product> search(ProductSearchCriteria criteria);
}
