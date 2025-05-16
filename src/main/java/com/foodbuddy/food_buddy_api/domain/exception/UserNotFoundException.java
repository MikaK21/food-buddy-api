package com.foodbuddy.food_buddy_api.domain.exception;

/**
 * Exception, wenn ein Benutzername in der Datenbank nicht gefunden wird.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User not found: " + username);
    }
}
