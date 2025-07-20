package com.inventory.inventory.infrastructure.persistence;

import com.inventory.inventory.domain.Inventory;
import com.inventory.inventory.domain.InventoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.relational.core.query.Criteria.where;

@Repository
@AllArgsConstructor
public class RDBInventoryRepository implements InventoryRepository {

    private final R2dbcEntityTemplate template;


    @Override
    public Mono<Void> save(Inventory inventory) {
        InventoryEntity entity = toEntity(inventory);
        return template.insert(entity).then();
    }

    @Override
    public Mono<Optional<Inventory>> findById(UUID id) {
        return template.select(InventoryEntity.class)
                .matching(Query.query(where("id").is(id)))
                .one()
                .map(inventoryEntity -> Optional.ofNullable(InventoryEntity.toDomain(inventoryEntity)))
                .switchIfEmpty(Mono.just(Optional.empty()));
    }

    @Override
    public Mono<Void> update(Inventory inventory) {
        InventoryEntity entity = toEntity(inventory);
        return template.update(entity).then();
    }

    private InventoryEntity toEntity(Inventory inventory) {
        return new InventoryEntity(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getQuantity(),
                inventory.getCreationDate(),
                inventory.getUpdateDate()
        );
    }

    private Inventory toDomain(InventoryEntity entity) {
        return new Inventory(
                entity.getId(),
                entity.getProductId(),
                entity.getQuantity(),
                entity.getCreationDate(),
                entity.getUpdateDate()
        );
    }
}
