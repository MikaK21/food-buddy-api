package com.foodbuddy.food_buddy_api.domain.exception;

public class ShopNotFoundException extends RuntimeException {
    public ShopNotFoundException(Long id) {
        super("Shop not found with ID: " + id);
    }
}
