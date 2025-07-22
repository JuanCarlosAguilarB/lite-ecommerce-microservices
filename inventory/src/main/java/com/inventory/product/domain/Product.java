package com.inventory.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Product {

    private UUID id;
    private String name;
    private String description;
    private double price;
}
