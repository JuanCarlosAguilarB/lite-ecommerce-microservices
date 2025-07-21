package com.inventory.inventory.services;

import com.inventory.warehouse.domain.*;
import com.inventory.inventory.domain.*;
import com.inventory.warehouse.services.WarehouseValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


// Todos, refactor
//public class InventoryCreator {
//
//    private InventoryRepository inventoryRepository;
//
//    public Mono<Void> save(UUID id, UUID productId, Optional<List<WarehouseRequested>> warehouses) {
//
//        // search if wharehouse exist
//        // get total amount of inventory
//        // create inventory in transaction
//        // create a Inventorywharehose
//        // update amount of inventory
//
//    }
//}


@Service
@AllArgsConstructor
@Slf4j
public class InventoryCreator {

    private final InventoryRepository inventoryRepository;
    private final InventoryWarehouseRepository inventoryWarehouseRepository;
    private final WarehouseValidationService warehouseValidationService;
    private final QuantityCalculatorService quantityCalculatorService;
    private final InventoryWarehouseFactory inventoryWarehouseFactory;
    private final TransactionalOperator transactionalOperator;


    public Mono<Void> save(UUID id, UUID productId, int quantity) {
        return createInventory(id, productId, quantity).then();
    }

    public Mono<Void> save(UUID id, UUID productId, Optional<List<WarehouseRequested>> warehouses) {

        return warehouses
                .map(warehouseList -> createInventoryWithWarehouses(id, productId, warehouseList))
                .orElse(createEmptyInventory(id, productId).then())
                .as(transactionalOperator::transactional)
                .onErrorMap(this::mapToInventoryCreationException);
    }

    private Mono<Void> createInventoryWithWarehouses(UUID id, UUID productId, List<WarehouseRequested> warehouseRequests) {
        return createInventory(id, productId)
                .flatMap(createdInventory ->
                        ensureWarehousesExists(warehouseRequests)
                                .then(Mono.defer(() -> {
                                    Integer totalQuantity = quantityCalculatorService.calculateTotalQuantity(warehouseRequests);
                                    return updateInventoryWithTotalQuantity(createdInventory, totalQuantity);
                                }))
                                .flatMap(updatedInventory ->
                                        createInventoryWarehouses(id, warehouseRequests)
                                )
                );
    }

    private Mono<Inventory> createInventory(UUID id, UUID productId, int quantity) {
        Inventory initialInventory = Inventory.create(id, productId, quantity);
        return inventoryRepository.save(initialInventory)
                .doOnSuccess(inventory -> log.info("Initial inventory created with ID {}", id))
                .doOnError(error -> log.error("Failed to create initial inventory for ID {}", id, error))
                .thenReturn(initialInventory);
    }

    private Mono<Inventory> createEmptyInventory(UUID id, UUID productId) {
        return createInventory(id, productId, 0);
    }


    private Mono<Inventory> createInventory(UUID id, UUID productId) {
        return createEmptyInventory(id, productId);
    }


    private Mono<Void> ensureWarehousesExists(List<WarehouseRequested> warehouseRequests) {
        List<UUID> warehouseIds = extractWarehouseIds(warehouseRequests);
        return warehouseValidationService.validateWarehousesExistInParallel(warehouseIds)
                .doOnSuccess(ignored -> log.info("Warehouses validation successful", warehouseIds))
                .doOnError(error -> log.error("Warehouse validation failed", warehouseIds, error));
    }

    private Mono<Void> updateInventoryWithTotalQuantity(Inventory inventory, Integer totalQuantity) {
        Inventory updatedInventory = inventory.updateQuantity(totalQuantity);
        return inventoryRepository.update(updatedInventory)
                .doOnSuccess(updated -> log.info("Inventory quantity updated", inventory.getId(), totalQuantity))
                .doOnError(error -> log.error("Failed to update inventory quantity", inventory.getId(), error));
    }

    private Mono<Void> createInventoryWarehouses(UUID inventoryId, List<WarehouseRequested> warehouseRequests) {
        List<InventoryWarehouse> inventoryWarehouses = inventoryWarehouseFactory.createInventoryWarehouses(inventoryId, warehouseRequests);

        return inventoryWarehouseRepository.saveAll(inventoryWarehouses)
                .doOnSuccess(created -> log.info("InventoryWarehouses created", inventoryId, created.size()))
                .doOnError(error -> log.error("Failed to create InventoryWarehouses", inventoryId, error))
                .then();
    }



    private List<UUID> extractWarehouseIds(List<WarehouseRequested> warehouseRequests) {
        return warehouseRequests.stream()
                .map(WarehouseRequested::id)
                .distinct()
                .collect(Collectors.toList());
    }

    private Throwable mapToInventoryCreationException(Throwable error) {
        if (error instanceof WarehouseNotFoundException) {
            return error;
        }
        return new InventoryCreationException("Failed to create inventory", error);
    }

}
