package com.products.api;

import com.products.services.products.ProductsCreator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class ProductsPutController {

    private final ProductsCreator productsCreator;

    @PutMapping("/api/v1/products/{id}")
    public Mono<ResponseEntity<?>> putProduct(@RequestParam UUID id, @RequestBody ProductsCreatorRequest request) {
        return productsCreator.save(
                id,
                request.name(),
                request.description(),
                request.price(),
                request.brandId(),
                request.categoryId()
                
        ).map( response -> ResponseEntity.ok().build());
    }
}

