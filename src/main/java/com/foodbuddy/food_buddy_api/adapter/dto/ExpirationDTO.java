package com.foodbuddy.food_buddy_api.adapter.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO zur Darstellung eines Verfallsdatums mit zugehöriger Menge.
 */
@Getter
@Setter
public class ExpirationDTO {
    private int amount;
    private LocalDate expirationDate;
    private String status; // NEU
}
