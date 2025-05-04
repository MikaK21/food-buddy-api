package com.foodbuddy.food_buddy_api.domain.exception;

public class CommunityNotFoundException extends RuntimeException {
    public CommunityNotFoundException(Long id) {
        super("Community not found with ID: " + id);
    }
}
