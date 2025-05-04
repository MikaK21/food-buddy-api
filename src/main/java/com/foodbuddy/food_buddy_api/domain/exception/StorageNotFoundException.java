package com.foodbuddy.food_buddy_api.domain.exception;

public class StorageNotFoundException extends RuntimeException {
    public StorageNotFoundException(Long id) {
        super("Storage not found with ID: " + id);
    }
}
