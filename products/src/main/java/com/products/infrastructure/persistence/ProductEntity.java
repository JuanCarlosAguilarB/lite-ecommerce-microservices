package com.products.infrastructure.persistence;

import com.products.products.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Table("products_entity")
@Getter
public class ProductEntity {

    private UUID id;
    private String name;
    private String description;
    private double price;
    private UUID brandId;
    private UUID categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductEntity fromDomain(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getBrandId(),
                product.getCategoryId(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    public static Product toDomain(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.brandId,
                entity.categoryId,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
                );
    }
}
