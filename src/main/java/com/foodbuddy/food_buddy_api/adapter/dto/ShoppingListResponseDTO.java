package com.foodbuddy.food_buddy_api.adapter.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShoppingListResponseDTO {
    private Long id;
    private String name;
    private String leaderUsername;
    private List<String> memberUsernames;
    private List<ShoppingListItemDTO> items;
}

