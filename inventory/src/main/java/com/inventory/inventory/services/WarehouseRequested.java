package com.inventory.inventory.services;

import java.util.UUID;

public record WarehouseRequested(UUID id, int quantity) {
}
