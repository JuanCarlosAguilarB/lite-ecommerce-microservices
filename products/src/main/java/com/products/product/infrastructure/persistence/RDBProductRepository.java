package com.products.product.infrastructure.persistence;

import com.products.product.domain.Product;
import com.products.product.domain.ProductRepository;
import com.products.product.domain.ProductSearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@AllArgsConstructor
public class RDBProductRepository implements ProductRepository {

    private final R2dbcEntityTemplate template;

    @Override
    public Mono<Void> save(Product product) {
        ProductEntity productEntity = ProductEntity.fromDomain(product);
        return  template.insert(productEntity).then();
    }

    @Override
    public Mono<Optional<Product>> FindById(UUID id) {
        return template.select(ProductEntity.class)
                .matching(query(where("id").is(id)))
                .one()
                .map(productEntity -> Optional.of(productEntity.toDomain(productEntity)))
                .switchIfEmpty(Mono.just(Optional.empty()));
    }

    @Override
    public Flux<Product> search(ProductSearchCriteria criteria) {
        Criteria base = Criteria.empty();

        if (criteria.name() != null) {
            base = base.and("name").like("%" + criteria.name() + "%");
        }
        if (criteria.brandId() != null) {
            base = base.and("brand_id").is(criteria.brandId());
        }
        if (criteria.categoryId() != null) {
            base = base.and("category_id").is(criteria.categoryId());
        }
        if (criteria.minPrice() != null) {
            base = base.and("price").greaterThanOrEquals(criteria.minPrice());
        }
        if (criteria.maxPrice() != null) {
            base = base.and("price").lessThanOrEquals(criteria.maxPrice());
        }

        return template.select(ProductEntity.class)
                .matching(Query.query(base))
                .all()
                .map(ProductEntity::toDomain);
    }
}
