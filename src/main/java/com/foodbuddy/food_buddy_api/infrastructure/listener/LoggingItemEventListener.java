package com.foodbuddy.food_buddy_api.infrastructure.listener;

import com.foodbuddy.food_buddy_api.application.event.ItemEventListener;
import com.foodbuddy.food_buddy_api.domain.event.ItemCreatedEvent;
import org.springframework.stereotype.Component;

/**
 * Event-Listener, der Item-Erstellungen in der Konsole protokolliert.
 */
@Component
public class LoggingItemEventListener implements ItemEventListener {

    @Override
    public void onItemCreated(ItemCreatedEvent event) {
        System.out.println("📝 Neues Item erstellt: " + event.getItem().getName());
    }
}
