package com.foodbuddy.food_buddy_api.domain.event;

import com.foodbuddy.food_buddy_api.domain.model.Item;

/**
 * Event, das beim Erstellen eines neuen Items ausgelöst wird.
 *
 * Enthält das betroffene Item als Payload.
 */
public class ItemCreatedEvent {
    private final Item item;

    public ItemCreatedEvent(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
