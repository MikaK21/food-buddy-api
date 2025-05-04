package com.foodbuddy.food_buddy_api.domain.exception;

public class ShoppingListNotFoundException extends RuntimeException {
    public ShoppingListNotFoundException(Long id) {
        super("ShoppingList not found with ID: " + id);
    }
}
