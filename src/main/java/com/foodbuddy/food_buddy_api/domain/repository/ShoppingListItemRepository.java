package com.foodbuddy.food_buddy_api.domain.repository;

import com.foodbuddy.food_buddy_api.domain.model.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA Repository für {@code ShoppingListItem}.
 */
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
}
