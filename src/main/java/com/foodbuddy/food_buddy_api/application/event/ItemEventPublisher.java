package com.foodbuddy.food_buddy_api.application.event;

import com.foodbuddy.food_buddy_api.domain.event.ItemCreatedEvent;

public interface ItemEventPublisher {
    void publish(ItemCreatedEvent event);
}
