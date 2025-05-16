package com.foodbuddy.food_buddy_api.domain.exception;

/**
 * Exception, wenn ein Benutzer nicht berechtigt ist, den angegebenen Shop zu bearbeiten.
 */
public class ShopPermissionDeniedException extends RuntimeException {
    public ShopPermissionDeniedException(String username, Long shopId) {
        super("User '" + username + "' is not allowed to modify shop with ID: " + shopId);
    }
}
