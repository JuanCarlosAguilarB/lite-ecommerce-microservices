package com.products.product.services;

import com.products.product.domain.Product;
import com.products.product.domain.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductsCreator {

    private final ProductRepository productRepository;

    public Mono<Void> save(
            UUID id,
            String name,
            String description,
            double price,
            UUID brandId,
            UUID categoryId
    ){
        Product product = Product.create(id, name, description, price, brandId, categoryId);
        return productRepository.save(product);
    }
}
