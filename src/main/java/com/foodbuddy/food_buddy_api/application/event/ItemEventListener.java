package com.foodbuddy.food_buddy_api.application.event;

import com.foodbuddy.food_buddy_api.domain.event.ItemCreatedEvent;

/**
 * Interface für Event-Listener, die auf Item-Erstellungen reagieren.
 */
public interface ItemEventListener {
    void onItemCreated(ItemCreatedEvent event);
}
