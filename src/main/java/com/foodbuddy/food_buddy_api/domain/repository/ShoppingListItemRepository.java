package com.foodbuddy.food_buddy.domain.repository;

import com.foodbuddy.food_buddy.domain.model.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
}
