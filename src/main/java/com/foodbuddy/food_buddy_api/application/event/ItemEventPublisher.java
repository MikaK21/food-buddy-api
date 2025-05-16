package com.foodbuddy.food_buddy_api.application.event;

import com.foodbuddy.food_buddy_api.domain.event.ItemCreatedEvent;

/**
 * Interface zur Veröffentlichung von {@code ItemCreatedEvent}.
 */
public interface ItemEventPublisher {
    void publish(ItemCreatedEvent event);
}
