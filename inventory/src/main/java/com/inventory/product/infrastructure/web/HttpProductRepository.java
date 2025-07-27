package com.inventory.product.infrastructure.web;

import com.inventory.product.domain.Product;
import com.inventory.product.domain.ProductGateway;
import com.inventory.product.domain.ProductNotFoundException;
import com.inventory.shared.infrastructure.web.WebClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
public class HttpProductRepository implements ProductGateway {


//    @Value("${products.url}") //we cannot do it bc values is injected after the constructor execution
//    private String baseUrl;
    private final WebClient productWebClient;
    private final String productByIdPath;

    public HttpProductRepository(WebClientConfig webClientConfig,  @Value("${products.url}") String baseUrl, @Value("${products.endpoint.by-id}")   String productByIdPath) {
        this.productWebClient = webClientConfig.buildWebClient(baseUrl);
        this.productByIdPath = productByIdPath;
    }

    @Override
    public Mono<Product> findById(UUID id) {

        String resolvedPath = productByIdPath.replace("{id}", id.toString());
        log.info("Calling Product Service URL: {}", resolvedPath);

        return productWebClient.get()
                .uri(resolvedPath)
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