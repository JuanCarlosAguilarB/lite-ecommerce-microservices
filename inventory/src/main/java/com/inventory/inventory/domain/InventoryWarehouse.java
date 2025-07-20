package com.inventory.inventory.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class InventoryWarehouse {
    private UUID id;
    private UUID inventoryId;
    private UUID warehouseId;
    private int quantity;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;

    public InventoryWarehouse(UUID id, UUID inventoryId, UUID warehouseId,  int quantity, LocalDateTime creationDate, LocalDateTime updateDate) {
        this.id = id;
        this.inventoryId = inventoryId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    public static InventoryWarehouse create(UUID inventoryId, UUID warehouseId, Integer quantity) {
        LocalDateTime now = LocalDateTime.now();
        return new InventoryWarehouse(UUID.randomUUID(), inventoryId, warehouseId, quantity, now, now);
    }
}
