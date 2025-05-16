package com.foodbuddy.food_buddy_api.domain.exception;

/**
 * Exception, die geworfen wird, wenn ein Item mit der gegebenen ID nicht existiert.
 */
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long id) {
        super("Item not found with ID: " + id);
    }
}
