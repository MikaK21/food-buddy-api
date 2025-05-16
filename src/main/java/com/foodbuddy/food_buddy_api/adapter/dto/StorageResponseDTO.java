package com.foodbuddy.food_buddy_api.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO zur Anzeige von Storage-Daten (ID und Name).
 */
@Getter
@AllArgsConstructor
public class StorageResponseDTO {
    private Long id;
    private String name;
}
