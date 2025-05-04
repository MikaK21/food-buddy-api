package com.foodbuddy.food_buddy_api.UnitTest.model;

import com.foodbuddy.food_buddy.domain.model.Shop;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShopTest {

    @Test
    void shouldCreateShopWithOwner() {
        MyUser user = new MyUser();
        user.setId(1L);
        user.setUsername("mk");

        Shop shop = new Shop();
        shop.setId(10L);
        shop.setName("Lidl");
        shop.setOwner(user);

        assertEquals(10L, shop.getId());
        assertEquals("Lidl", shop.getName());
        assertEquals(user, shop.getOwner());
    }
}
