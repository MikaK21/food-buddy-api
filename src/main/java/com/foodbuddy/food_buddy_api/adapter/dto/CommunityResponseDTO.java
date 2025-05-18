package com.foodbuddy.food_buddy_api.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommunityResponseDTO {
    private Long id;
    private String name;
    private UserResponseDTO leader;
    private List<UserResponseDTO> members;
    private List<StorageResponseDTO> storages;
}
