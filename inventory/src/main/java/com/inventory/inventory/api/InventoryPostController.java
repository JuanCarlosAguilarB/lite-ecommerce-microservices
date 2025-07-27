package com.inventory.inventory.api;

import com.inventory.inventory.services.InventoryPurchaseService;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class InventoryPostController {

    private final InventoryPurchaseService purchaseService;

    @PostMapping("/purchase")
    public Mono<ResponseEntity<Map<String, String>>> purchase(@RequestBody PurchaseRequest request) {
        if (request.productId() == null || request.quantity() == null) {
            return Mono.just(ResponseEntity.badRequest().body(Map.of(
                    "error", "A product id and quantity is required."
            )));
        }

        return purchaseService.purchase(request.productId(), request.quantity())
                .thenReturn(ResponseEntity.ok(Map.of("message", "Purchase successful")))
                .onErrorResume(IllegalArgumentException.class, ex ->
                        Mono.just(ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()))))
                .onErrorResume(IllegalStateException.class, ex ->
                        Mono.just(ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()))))
                .onErrorResume(ChangeSetPersister.NotFoundException.class, ex ->
                        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("error", ex.getMessage()))));
    }
}

record PurchaseRequest(UUID productId, Integer quantity) {}
