package com.inventory.product.domain;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductGateway {

    Mono<Product> findById(UUID id);
}
