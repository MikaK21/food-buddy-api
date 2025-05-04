package com.foodbuddy.food_buddy_api.domain.event;

import com.foodbuddy.food_buddy_api.domain.model.Item;

public class ItemCreatedEvent {
    private final Item item;

    public ItemCreatedEvent(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
