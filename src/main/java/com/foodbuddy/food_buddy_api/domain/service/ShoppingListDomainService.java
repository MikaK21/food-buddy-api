package com.foodbuddy.food_buddy_api.domain.service;

import com.foodbuddy.food_buddy_api.domain.model.ShoppingList;
import com.foodbuddy.food_buddy_api.domain.model.Shop;
import org.springframework.stereotype.Component;

/**
 * Domänenspezifische Regeln für Einkaufslisten.
 *
 * Beinhaltet:
 * - Prüfung auf Duplikate innerhalb einer Liste
 * - Limitierung auf maximal 50 Einträge pro Liste
 */
@Component
public class ShoppingListDomainService {

    private static final int MAX_ITEMS = 50;

    public boolean isBelowItemLimit(ShoppingList list) {
        return list.getItems().size() < MAX_ITEMS;
    }

    public boolean isDuplicateItem(ShoppingList list, String name, int amount, Shop shop) {
        return list.getItems().stream().anyMatch(existing ->
                existing.getName().equalsIgnoreCase(name)
                        && existing.getAmount() == amount
                        && ((existing.getShop() == null && shop == null)
                        || (existing.getShop() != null && existing.getShop().equals(shop)))
        );
    }
}
