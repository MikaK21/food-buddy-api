package com.foodbuddy.food_buddy_api.UnitTest.model;

import com.foodbuddy.food_buddy_api.domain.model.Shop;
import com.foodbuddy.food_buddy_api.domain.model.ShoppingList;
import com.foodbuddy.food_buddy_api.domain.model.ShoppingListItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingListItemTest {

    @Test
    void shouldCreateShoppingListItem() {
        Shop shop = new Shop();
        shop.setId(1L);
        shop.setName("Rewe");

        ShoppingList list = new ShoppingList();
        list.setId(10L);
        list.setName("Woche");

        ShoppingListItem item = new ShoppingListItem();
        item.setId(100L);
        item.setName("Butter");
        item.setAmount(2);
        item.setShop(shop);
        item.setShoppingList(list);

        assertEquals(100L, item.getId());
        assertEquals("Butter", item.getName());
        assertEquals(2, item.getAmount());
        assertEquals(shop, item.getShop());
        assertEquals(list, item.getShoppingList());
    }
}
