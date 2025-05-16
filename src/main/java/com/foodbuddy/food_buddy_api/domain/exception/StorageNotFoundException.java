package com.foodbuddy.food_buddy_api.domain.exception;

/**
 * Exception, wenn ein Speicher (Storage) mit der gegebenen ID nicht existiert.
 */
public class StorageNotFoundException extends RuntimeException {
    public StorageNotFoundException(Long id) {
        super("Storage not found with ID: " + id);
    }
}
