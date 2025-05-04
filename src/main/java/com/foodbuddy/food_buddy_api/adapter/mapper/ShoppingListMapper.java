package com.foodbuddy.food_buddy.adapter.mapper;

import com.foodbuddy.food_buddy.adapter.dto.ShoppingListItemDTO;
import com.foodbuddy.food_buddy.adapter.dto.ShoppingListResponseDTO;
import com.foodbuddy.food_buddy.domain.model.ShoppingList;
import com.foodbuddy.food_buddy.domain.model.ShoppingListItem;

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
