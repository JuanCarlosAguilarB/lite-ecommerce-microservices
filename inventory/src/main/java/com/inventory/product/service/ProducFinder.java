package com.inventory.product.service;

import com.inventory.product.domain.Product;
import com.inventory.product.domain.ProductGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@Service
public class ProducFinder {

    private final ProductGateway productGateway;

    public Mono<Product> findById(UUID id) {
        return productGateway.findById(id);
    }
}
