package com.inventory.warehouse.domain;

import java.util.UUID;

public class Warehouse {
    private UUID id;
    private String name;
    private String description;
    private String direction;

    public Warehouse(UUID id, String name, String description, String direction) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.direction = direction;
    }

}
