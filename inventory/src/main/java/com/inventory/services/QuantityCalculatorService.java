package com.inventory.services;

import com.inventory.inventory.domain.InventoryWarehouse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuantityCalculatorService {

    public Integer calculateTotalQuantity(List<WarehouseRequested> warehouseRequests) {
        return warehouseRequests.stream()
                .mapToInt(WarehouseRequested::quantity)
                .sum();
    }
}
