package com.products.product.api;

import java.util.UUID;

public record ProductsCreatorRequest(
        UUID id,
        String name,
        String description,
        double price,
        UUID brandId,
        UUID categoryId
){}
