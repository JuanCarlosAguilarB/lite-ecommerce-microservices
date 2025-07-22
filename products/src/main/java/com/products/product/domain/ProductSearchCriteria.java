package com.products.product.domain;

import java.util.UUID;

public record ProductSearchCriteria(
        String name,
        UUID brandId,
        UUID categoryId,
        Double minPrice,
        Double maxPrice
) {}