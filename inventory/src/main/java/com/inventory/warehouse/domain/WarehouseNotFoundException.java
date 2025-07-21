package com.inventory.warehouse.domain;

import java.util.List;
import java.util.UUID;

public class WarehouseNotFoundException extends RuntimeException {
    private final List<UUID> missingWarehouseIds;

    public WarehouseNotFoundException(List<UUID> missingWarehouseIds) {
        super("Warehouses not found: " + missingWarehouseIds);
        this.missingWarehouseIds = missingWarehouseIds;
    }

    public List<UUID> getMissingWarehouseIds() { return missingWarehouseIds; }
}