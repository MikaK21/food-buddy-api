package com.foodbuddy.food_buddy_api.adapter.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShopDTO {
    private Long id;
    private String name;
    private List<UserResponseDTO> users;

    public ShopDTO(Long id, String name, List<UserResponseDTO> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }

    // Getter & Setter
}
