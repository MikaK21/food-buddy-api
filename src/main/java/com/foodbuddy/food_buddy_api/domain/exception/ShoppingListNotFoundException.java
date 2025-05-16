package com.foodbuddy.food_buddy_api.domain.exception;

/**
 * Exception, wenn eine Einkaufsliste mit der angegebenen ID nicht existiert.
 */
public class ShoppingListNotFoundException extends RuntimeException {
    public ShoppingListNotFoundException(Long id) {
        super("ShoppingList not found with ID: " + id);
    }
}
