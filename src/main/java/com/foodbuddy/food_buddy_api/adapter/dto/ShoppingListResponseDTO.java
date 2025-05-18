package com.foodbuddy.food_buddy_api.adapter.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO zur Ausgabe einer vollständigen Einkaufsliste inklusive Items und Mitglieder.
 */
@Getter
@Setter
public class ShoppingListResponseDTO {
    private Long id;
    private String name;
    private UserResponseDTO leader;
    private List<UserResponseDTO> members;
    private List<ShoppingListItemDTO> items;
}

