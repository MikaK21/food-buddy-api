package com.foodbuddy.food_buddy_api.domain.exception;

public class ShopPermissionDeniedException extends RuntimeException {
    public ShopPermissionDeniedException(String username, Long shopId) {
        super("User '" + username + "' is not allowed to modify shop with ID: " + shopId);
    }
}
