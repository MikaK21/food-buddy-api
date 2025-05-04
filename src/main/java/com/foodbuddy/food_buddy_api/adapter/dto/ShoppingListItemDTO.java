package com.foodbuddy.food_buddy_api.adapter.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingListItemDTO {
    private Long id;
    private String name;
    private int amount;
    private Long shopId;
    private String shopName;
}

