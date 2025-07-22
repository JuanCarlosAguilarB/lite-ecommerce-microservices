package com.inventory.inventory.api;

import com.inventory.inventory.services.InventoryFinder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class InventoryPatchController {

    private final InventoryFinder inventoryFinder;

    @PatchMapping("/v1/inventory")
    public Mono<ResponseEntity<Map<String, Object>>> updateQuantity(
            @RequestParam(required = false) UUID productId,
            @RequestBody UpdateInventoryRequest request
    ) {
        if (productId == null) {
            return Mono.just(ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Falta el parámetro productId")));
        }

        return inventoryFinder.updateQuantity(productId, request.quantity())
                .thenReturn(ResponseEntity.ok(Map.of("message", "Inventario actualizado exitosamente")));
    }

}


record UpdateInventoryRequest(Integer quantity) {}
