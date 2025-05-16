package com.foodbuddy.food_buddy_api.application.event;

import com.foodbuddy.food_buddy_api.domain.event.ItemCreatedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Standardimplementierung des {@code ItemEventPublisher}.
 *
 * Benachrichtigt alle registrierten {@code ItemEventListener}.
 */
@Component
public class DefaultItemEventPublisher implements ItemEventPublisher {

    private final List<ItemEventListener> listeners;

    public DefaultItemEventPublisher(List<ItemEventListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void publish(ItemCreatedEvent event) {
        for (ItemEventListener listener : listeners) {
            listener.onItemCreated(event);
        }
    }
}
