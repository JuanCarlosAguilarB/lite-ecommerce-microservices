package com.inventory.inventory.services;

import com.inventory.inventory.domain.Inventory;
import com.inventory.inventory.domain.InventoryNotFoundException;
import com.inventory.inventory.domain.InventoryRepository;
import com.inventory.inventory.domain.InventoryResponse;
import com.inventory.product.service.ProducFinder;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class InventoryFinder {

    private final InventoryRepository inventoryRepository;
    private final ProducFinder productFinder;

    public Mono<InventoryResponse> getInventoryByProductId(UUID productId) {
        return inventoryRepository.findByProductId(productId)
                .flatMap(optionalInventory -> optionalInventory
                        .map(Mono::just)
                        .orElseGet(() -> Mono.error(new InventoryNotFoundException("Inventory not found")))
                )
                .flatMap(inventory -> productFinder.findById(inventory.getProductId())
                        .map(product -> InventoryResponse.from(inventory, product))
                );
    }

    public Mono<Void> updateQuantity(UUID productId, Integer newQuantity) {
        return inventoryRepository.findByProductId(productId)
                .flatMap(optionalInventory -> optionalInventory
                        .map(Mono::just)
                        .orElseGet(() -> Mono.error(new InventoryNotFoundException("Inventory not found")))
                )
                .flatMap(inventory -> {
                    Inventory updated = inventory.updateQuantity(newQuantity);
                    return inventoryRepository.save(updated)
                            .then();
                });
    }

}
