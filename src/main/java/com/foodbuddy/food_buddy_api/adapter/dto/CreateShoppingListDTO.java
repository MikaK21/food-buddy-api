package com.foodbuddy.food_buddy_api.adapter.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO zur Erstellung einer neuen Einkaufsliste.
 */
@Getter
@Setter
public class CreateShoppingListDTO {
    private String name;
}
