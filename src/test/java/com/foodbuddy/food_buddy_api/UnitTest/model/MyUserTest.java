package com.foodbuddy.food_buddy_api.UnitTest.model;

import org.junit.jupiter.api.Test;

class MyUserTest {

    @Test
    void shouldStoreUserInformation() {
        MyUser user = new MyUser();
        user.setId(5L);
        user.setUsername("mk");
        user.setPassword("secure");
        user.setEmail("mk@food.de");

        assertEquals(5L, user.getId());
        assertEquals("mk", user.getUsername());
        assertEquals("secure", user.getPassword());
        assertEquals("mk@food.de", user.getEmail());
    }
}
