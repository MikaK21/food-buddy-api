package com.foodbuddy.food_buddy_api.domain.repository;

import com.foodbuddy.food_buddy_api.domain.model.MyUser;
import com.foodbuddy.food_buddy_api.domain.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA Repository für {@code ShoppingList}.
 *
 * Unterstützt die Suche nach Listen, bei denen ein Benutzer entweder Leader oder Mitglied ist.
 */
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

    List<ShoppingList> findByLeaderOrMembersContains(MyUser leader, MyUser member);
}
