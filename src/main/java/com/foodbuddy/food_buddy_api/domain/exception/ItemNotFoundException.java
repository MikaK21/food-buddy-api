package com.foodbuddy.food_buddy_api.domain.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long id) {
        super("Item not found with ID: " + id);
    }
}
