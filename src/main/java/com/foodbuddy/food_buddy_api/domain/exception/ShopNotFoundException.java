package com.foodbuddy.food_buddy_api.domain.exception;

/**
 * Exception, wenn ein Shop mit der gegebenen ID nicht existiert.
 */
public class ShopNotFoundException extends RuntimeException {
    public ShopNotFoundException(Long id) {
        super("Shop not found with ID: " + id);
    }
}
