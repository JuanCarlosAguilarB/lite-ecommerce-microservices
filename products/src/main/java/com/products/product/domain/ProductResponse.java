package com.products.product.domain;

import java.util.UUID;

public record ProductResponse(UUID id, String name, String description, Double price, UUID brandId, UUID categoryId) { }
