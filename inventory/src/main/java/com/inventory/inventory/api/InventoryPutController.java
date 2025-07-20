package com.inventory.inventory.api;

import com.inventory.inventory.services.InventoryCreator;
import com.inventory.inventory.services.WarehouseRequested;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class InventoryPutController {

    private final InventoryCreator inventoryCreator;

    @PutMapping("/api/v1/inventories/{id}")
    public Mono<ResponseEntity<?>> save(@PathVariable UUID id, @RequestBody InventoryRequest request) {
        return inventoryCreator.save(id, request.productId(), request.warehouses()).map(ResponseEntity::ok);
    }
}

record InventoryRequest(UUID productId, Optional<List<WarehouseRequested>> warehouses){}