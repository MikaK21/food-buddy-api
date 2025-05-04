package com.foodbuddy.food_buddy_api.UnitTest.model;

import org.junit.jupiter.api.Test;

public class StorageTest {

    @Test
    void shouldCreateStorageWithCommunity() {
        Community community = new Community();
        community.setId(1L);
        community.setName("WG Köln");

        Storage storage = new Storage();
        storage.setId(10L);
        storage.setName("Kühlschrank");
        storage.setCommunity(community);

        assertEquals(10L, storage.getId());
        assertEquals("Kühlschrank", storage.getName());
        assertEquals(community, storage.getCommunity());
    }
}
