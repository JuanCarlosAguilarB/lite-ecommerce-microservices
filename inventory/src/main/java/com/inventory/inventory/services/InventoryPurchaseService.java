package com.inventory.inventory.services;

import com.inventory.inventory.domain.Inventory;
import com.inventory.inventory.domain.InventoryNotFoundException;
import com.inventory.inventory.domain.InventoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class InventoryPurchaseService {

    private final InventoryRepository inventoryRepository;

    public Mono<Void> purchase(UUID productId, int quantityToBuy) {
        if (quantityToBuy <= 0) {
            return Mono.error(new IllegalArgumentException("The stock should be greater than 0"));
        }

        return inventoryRepository.findByProductId(productId)
                .flatMap(optionalInventory -> optionalInventory
                        .map(Mono::just)
                        .orElseGet(() -> Mono.error(new InventoryNotFoundException("Inventory not found")))
                )
                .flatMap(inventory -> {
                    if (inventory.getQuantity() < quantityToBuy) {
                        return Mono.error(new IllegalStateException("Stock insuficiente para la compra."));
                    }

                    Inventory updatedInventory = inventory.updateQuantity(inventory.getQuantity() - quantityToBuy);

                    return inventoryRepository.save(updatedInventory)
                            .then();
                });
    }
}
