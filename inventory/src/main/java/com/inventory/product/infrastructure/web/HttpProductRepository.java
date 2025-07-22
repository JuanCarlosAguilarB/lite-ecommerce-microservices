package com.inventory.product.infrastructure.web;

import com.inventory.product.domain.Product;
import com.inventory.product.domain.ProductGateway;
import com.inventory.product.domain.ProductNotFoundException;
import com.inventory.shared.infrastructure.web.WebClientConfig;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.UUID;

import java.util.concurrent.TimeoutException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class HttpProductRepository implements ProductGateway {

    private final String baseUrl = "http://localhost:8080";
    private final WebClient productWebClient;

    public HttpProductRepository(WebClientConfig webClientConfig) {
        this.productWebClient = webClientConfig.buildWebClient(baseUrl);
    }

    @Override
    public Mono<Product> findById(UUID id) {
        return productWebClient.get()
                .uri("/api/v1/products/{id}", id)
//                .retrieve()                                // 4xx/5xx will be exceptions
//                .bodyToMono(ProductResponse.class)
//                .map(ProductResponse::toDomain)
//                // --- recilency ---
                .exchangeToMono(resp -> {
                    if (resp.statusCode().is2xxSuccessful()) {
                        return resp.bodyToMono(ProductResponse.class)
                                .map(ProductResponse::toDomain);
                    }
                    if (resp.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(new ProductNotFoundException(id));
                    }
                    return resp.createException().flatMap(Mono::error);
                })
                .timeout(Duration.ofSeconds(3))            // fail‑fast if it takes more than 3 seconds
                .retryWhen(
                        Retry.backoff(2, Duration.ofMillis(200))   // 2 retres with 200ms delay
                                .filter(this::isRetryable)            // filter timeouts and 5xx errors
                );
    }

    private boolean isRetryable(Throwable t) {
        return t instanceof TimeoutException ||
                (t instanceof WebClientResponseException ex &&
                        ex.getStatusCode().is5xxServerError());
    }
}

record ProductResponse(Product data) {
    Product toDomain() { return data; }
}