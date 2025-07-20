package com.inventory.inventory.infrastructure.persistence;

import com.inventory.inventory.domain.InventoryWarehouse;
import com.inventory.inventory.domain.InventoryWarehouseRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class RDBInventoryWarehouseRepository implements InventoryWarehouseRepository {

    private final R2dbcEntityTemplate template;

    @Override
    public Mono<List<InventoryWarehouse>> saveAll(List<InventoryWarehouse> inventoryWarehouses) {
        return Flux.fromIterable(inventoryWarehouses)
                .flatMap(template::insert)
                .collectList();
    }

    @Override
    public Flux<InventoryWarehouse> findByInventoryId(UUID inventoryId) {
        return template.select(Query.query(Criteria.where("inventory_id").is(inventoryId)), InventoryWarehouse.class);
    }
}