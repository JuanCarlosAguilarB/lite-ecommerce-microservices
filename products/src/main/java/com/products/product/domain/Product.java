package com.products.product.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Product {

    private UUID id;
    private String name;
    private String description;
    private double price;
    private UUID brandId;
    private UUID categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(UUID id, String name, String description, double price, UUID brandId, UUID categoryId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public static Product create(UUID id,  String name, String description, double price, UUID brandId, UUID categoryId) {
        return new Product(
                id,
                name,
                description,
                price,
                brandId,
                categoryId,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
