package com.inventory.inventory.domain;

public class InventoryCreationException extends RuntimeException {
    public InventoryCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}