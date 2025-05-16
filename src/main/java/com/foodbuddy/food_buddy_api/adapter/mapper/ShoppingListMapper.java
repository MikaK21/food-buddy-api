package com.foodbuddy.food_buddy_api.adapter.mapper;

import com.foodbuddy.food_buddy_api.adapter.dto.ShoppingListItemDTO;
import com.foodbuddy.food_buddy_api.adapter.dto.ShoppingListResponseDTO;
import com.foodbuddy.food_buddy_api.domain.model.MyUser;
import com.foodbuddy.food_buddy_api.domain.model.ShoppingList;
import com.foodbuddy.food_buddy_api.domain.model.ShoppingListItem;

/**
 * Mapper zur Umwandlung zwischen {@code ShoppingList} / {@code ShoppingListItem}
 * und deren DTO-Darstellungen für die API.
 *
 * - Wandelt Einkaufslisten in DTOs mit Mitgliedern und Items
 * - Wandelt Listeneinträge inkl. Shop-Zuordnung in DTOs
 */
public class ShoppingListMapper {

    public static ShoppingListResponseDTO toDto(ShoppingList list) {
        ShoppingListResponseDTO dto = new ShoppingListResponseDTO();

        dto.setId(list.getId());
        dto.setName(list.getName());
        dto.setLeaderUsername(list.getLeader().getUsername());

        dto.setMemberUsernames(
                list.getMembers().stream()
                        .map(MyUser::getUsername)
                        .toList()
        );

        dto.setItems(
                list.getItems().stream()
                        .map(ShoppingListMapper::toItemDto)
                        .toList()
        );

        return dto;
    }

    public static ShoppingListItemDTO toItemDto(ShoppingListItem item) {
        ShoppingListItemDTO dto = new ShoppingListItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setAmount(item.getAmount());

        if (item.getShop() != null) {
            dto.setShopId(item.getShop().getId());
            dto.setShopName(item.getShop().getName());
        }

        return dto;
    }
}
