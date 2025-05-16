package com.foodbuddy.food_buddy_api.adapter.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO für Nährwertinformationen eines Produkts.
 */
@Getter
@Setter
public class NutritionInfoDTO {

    private double kcal;
    private double carbohydrates;
    private double sugar;
    private double protein;
    private double fat;
    private double saturatedFat;
    private double salt;
    private double fiber;
}
