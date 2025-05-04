package com.foodbuddy.food_buddy_api.UnitTest.model;

import com.foodbuddy.food_buddy.domain.model.ShoppingList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingListTest {

    @Test
    void shouldCreateShoppingListCorrectly() {
        MyUser user = new MyUser();
        user.setId(1L);
        user.setUsername("mk");

        ShoppingList list = new ShoppingList();
        list.setName("Wocheneinkauf");
        list.setLeader(user);
        list.addMember(user);

        assertEquals("Wocheneinkauf", list.getName());
        assertEquals(user, list.getLeader());
        assertTrue(list.hasMember(user));
        assertTrue(list.isLeader(user));
    }

    @Test
    void shouldAddAndRemoveMembers() {
        MyUser user = new MyUser();
        user.setId(10L);

        ShoppingList list = new ShoppingList();
        list.addMember(user);

        assertTrue(list.hasMember(user));

        list.removeMember(user);
        assertFalse(list.hasMember(user));
    }

    @Test
    void shouldRecognizeLeader() {
        MyUser leader = new MyUser();
        leader.setId(5L);

        ShoppingList list = new ShoppingList();
        list.setLeader(leader);

        assertTrue(list.isLeader(leader));

        MyUser outsider = new MyUser();
        outsider.setId(99L);
        assertFalse(list.isLeader(outsider));
    }

    @Test
    void hasMember_shouldReturnTrueForLeader() {
        MyUser leader = new MyUser();
        leader.setId(1L);

        ShoppingList list = new ShoppingList();
        list.setLeader(leader);

        assertTrue(list.hasMember(leader));
    }
}
