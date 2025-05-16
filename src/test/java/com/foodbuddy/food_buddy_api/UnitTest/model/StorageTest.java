package com.foodbuddy.food_buddy_api.UnitTest.model;

import com.foodbuddy.food_buddy_api.domain.model.Community;
import com.foodbuddy.food_buddy_api.domain.model.Storage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit Test für {@link Storage}.
 *
 * Testet:
 * - Erstellung mit Verknüpfung zu einer Community
 */
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
