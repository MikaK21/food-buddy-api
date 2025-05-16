package com.foodbuddy.food_buddy_api.domain.exception;

/**
 * Exception, wenn ein Eintrag in einer Einkaufsliste nicht gefunden wurde.
 */
public class ShoppingListItemNotFoundException extends RuntimeException {
    public ShoppingListItemNotFoundException(Long id) {
        super("ShoppingListItem not found with ID: " + id);
    }
}
