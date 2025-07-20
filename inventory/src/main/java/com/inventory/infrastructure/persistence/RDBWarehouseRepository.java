package com.inventory.infrastructure.persistence;

import com.inventory.inventory.domain.Warehouse;
import com.inventory.inventory.domain.WarehouseRepository;
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
public class RDBWarehouseRepository implements WarehouseRepository {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<Warehouse> findAllById(List<UUID> ids) {
        return template.select(Query.query(Criteria.where("id").in(ids)), Warehouse.class);
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return template.exists(Query.query(Criteria.where("id").is(id)), Warehouse.class);
    }
}