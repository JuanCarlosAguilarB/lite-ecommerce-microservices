package com.inventory.inventory.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Inventory {
    private UUID id;
    private UUID productId;
    private Integer quantity;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;

    public Inventory(UUID id, UUID productId, Integer quantity, LocalDateTime creationDate, LocalDateTime updateDate) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    public Inventory updateQuantity(Integer newQuantity) {
        return new Inventory(this.id, this.productId, newQuantity, this.creationDate, LocalDateTime.now());
    }

    public static Inventory create(UUID id, UUID productId, Integer quantity) {
        return new Inventory(id, productId, quantity, LocalDateTime.now(), LocalDateTime.now());
    }
}
