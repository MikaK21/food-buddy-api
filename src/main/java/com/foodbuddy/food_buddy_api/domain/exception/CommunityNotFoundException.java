package com.foodbuddy.food_buddy_api.domain.exception;

/**
 * Exception, die geworfen wird, wenn eine Community mit der gegebenen ID nicht existiert.
 */
public class CommunityNotFoundException extends RuntimeException {
    public CommunityNotFoundException(Long id) {
        super("Community not found with ID: " + id);
    }
}
