package com.inventory.shared.infrastructure.excepciones;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String code,
        String message,
        String path,
        LocalDateTime timestamp
) {}