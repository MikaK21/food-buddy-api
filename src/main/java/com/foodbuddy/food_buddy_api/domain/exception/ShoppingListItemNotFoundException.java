package com.foodbuddy.food_buddy_api.domain.exception;

public class ShoppingListItemNotFoundException extends RuntimeException {
    public ShoppingListItemNotFoundException(Long id) {
        super("ShoppingListItem not found with ID: " + id);
    }
}
