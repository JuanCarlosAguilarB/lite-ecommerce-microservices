package com.inventory.inventory.services;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityCalculatorService {

    public Integer calculateTotalQuantity(List<WarehouseRequested> warehouseRequests) {
        return warehouseRequests.stream()
                .mapToInt(WarehouseRequested::quantity)
                .sum();
    }
}
