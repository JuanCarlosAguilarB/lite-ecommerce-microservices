package com.inventory.inventory.domain;

import java.util.UUID;

public record InventoryResponse(
        UUID id,
        UUID productId,
        Integer quantity
) {
    public static InventoryResponse from(Inventory inventory) {
        return new InventoryResponse(inventory.getId(), inventory.getProductId(), inventory.getQuantity());
    }
}

