package com.inventory.warehouse.services;

import com.inventory.warehouse.domain.WarehouseNotFoundException;
import com.inventory.warehouse.domain.WarehouseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WarehouseValidationService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseValidationService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Mono<Void> validateWarehousesExistInParallel(List<UUID> warehouseIds) {
        return Flux.fromIterable(warehouseIds)
                .distinct()
                .flatMap(warehouseId ->
                        warehouseRepository.existsById(warehouseId)
                                .map(exists -> exists ? Optional.<UUID>empty() : Optional.of(warehouseId))
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collectList()
                .flatMap(missingIds -> {
                    if (!missingIds.isEmpty()) {
                        return Mono.error(new WarehouseNotFoundException(missingIds));
                    }
                    return Mono.empty();
                });
    }
}
