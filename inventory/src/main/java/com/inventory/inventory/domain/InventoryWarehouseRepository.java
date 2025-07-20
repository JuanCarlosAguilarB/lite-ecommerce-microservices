package com.inventory.inventory.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface InventoryWarehouseRepository {

    Mono<List<InventoryWarehouse>> saveAll(List<InventoryWarehouse> inventoryWarehouses);
    Flux<InventoryWarehouse> findByInventoryId(UUID inventoryId);

}
