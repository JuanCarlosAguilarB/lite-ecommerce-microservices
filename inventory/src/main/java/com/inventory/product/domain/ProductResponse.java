package com.inventory.product.domain;

import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        double price
)
 {

}
