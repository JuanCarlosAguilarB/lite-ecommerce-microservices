package com.inventory.inventory.domain;

import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository {

    Mono<Void> save(Inventory inventory);
    Mono<Optional<Inventory>> findById(UUID id);
    Mono<Void> update(Inventory inventory);

}
