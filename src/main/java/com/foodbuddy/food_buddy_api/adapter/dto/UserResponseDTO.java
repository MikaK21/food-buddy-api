package com.foodbuddy.food_buddy_api.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
}
