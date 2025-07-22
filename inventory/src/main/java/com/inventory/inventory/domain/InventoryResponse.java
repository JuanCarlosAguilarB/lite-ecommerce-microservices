package com.inventory.inventory.domain;

import com.inventory.product.domain.Product;
import com.inventory.product.domain.ProductResponse;

import java.util.UUID;

public record InventoryResponse(
        UUID id,
        UUID productId,
        Integer quantity,
        ProductResponse product

) {
    public static InventoryResponse from(Inventory inventory, Product product) {
        return new InventoryResponse(inventory.getId(),
                inventory.getProductId(),
                inventory.getQuantity(),
                new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice())
        );
    }
}

