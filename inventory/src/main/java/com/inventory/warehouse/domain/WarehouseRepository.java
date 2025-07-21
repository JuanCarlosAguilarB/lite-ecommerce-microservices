package com.inventory.warehouse.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface WarehouseRepository {
    Flux<Warehouse> findAllById(List<UUID> ids);
    Mono<Boolean> existsById(UUID id);
}
