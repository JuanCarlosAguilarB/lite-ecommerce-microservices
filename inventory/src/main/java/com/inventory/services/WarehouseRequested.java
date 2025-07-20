package com.inventory.services;

import java.util.UUID;

public record WarehouseRequested(UUID id, int quantity) {
}
