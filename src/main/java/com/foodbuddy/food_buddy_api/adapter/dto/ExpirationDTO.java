package com.foodbuddy.food_buddy.adapter.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExpirationDTO {

    private int amount;
    private LocalDate expirationDate;
}
