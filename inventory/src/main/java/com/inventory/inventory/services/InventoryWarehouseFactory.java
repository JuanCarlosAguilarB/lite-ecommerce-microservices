package com.inventory.inventory.services;

import com.inventory.inventory.domain.InventoryWarehouse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class InventoryWarehouseFactory {

    public List<InventoryWarehouse> createInventoryWarehouses(UUID inventoryId, List<WarehouseRequested> warehouseRequests) {
        return warehouseRequests.stream()
                .map(request -> InventoryWarehouse.create(inventoryId, request.id(), request.quantity()))
                .collect(Collectors.toList());
    }
}
