package com.inventory.shared.infrastructure.excepciones;

import com.inventory.inventory.domain.InventoryNotFoundException;
import com.inventory.product.domain.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InventoryNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInventoryNotFound(
            InventoryNotFoundException ex,
            ServerWebExchange exchange) {

        log.warn("Inventory not found: {}", ex.getMessage());

        var body = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "INVENTORY_NOT_FOUND",
                ex.getMessage(),
                exchange.getRequest().getPath().value(),
                LocalDateTime.now());

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(body));
    }

//    NoResourceFoundException
    @ExceptionHandler(NoResourceFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleNoResourceFoundException(
            NoResourceFoundException ex,
            ServerWebExchange exchange) {

        log.warn("Not found resource: {}", ex.getMessage());

        var body = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND_RESOURCE",
                ex.getMessage(),
                exchange.getRequest().getPath().value(),
                LocalDateTime.now());

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(body));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleProductNotFound(
            ProductNotFoundException ex,
            ServerWebExchange exchange) {

        log.warn("Producto no encontrado: {}", ex.getMessage());

        var body = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "PRODUCT_NOT_FOUND",
                ex.getMessage(),
                exchange.getRequest().getPath().value(),
                LocalDateTime.now());

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(body));
    }

    /* ───────────────────────────────────────────────────────────────
     *  Catch‑all to handle any other exception not handled
     * ─────────────────────────────────────────────────────────────── */
    @ExceptionHandler(Throwable.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneric(
            Throwable ex,
            ServerWebExchange exchange) {

        log.error("Error no controlado", ex);

        var body = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                ex.getMessage(),
                exchange.getRequest().getPath().value(),
                LocalDateTime.now());

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body));
    }
}