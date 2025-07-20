package com.inventory.inventory.api;

import com.inventory.inventory.domain.InventoryNotFoundException;
import com.inventory.inventory.services.InventoryFinder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/inventory")
@AllArgsConstructor
public class InventoryGetController {

    private final InventoryFinder inventoryFinder;

    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> getByProductId(
            @RequestParam(required = false) UUID productId
    ) {
        if (productId == null) {
            return Mono.just(ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "missing product id")));
        }

        return inventoryFinder.getInventoryByProductId(productId)
                .map(response -> ResponseEntity.ok(Map.of("data", response)));
    }
}

