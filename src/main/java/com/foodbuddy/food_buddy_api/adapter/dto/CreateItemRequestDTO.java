package com.foodbuddy.food_buddy.adapter.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateItemDTO {

    private Long storageId;

    private String name;
    private String brand;
    private String barcode;
    private String category;

    private double quantityValue;
    private Unit quantityUnit;

    private List<ExpirationDTO> expirations;

    private NutritionInfoDTO nutritionInfo;
}
