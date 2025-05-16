package com.foodbuddy.food_buddy_api.adapter.dto;

import com.foodbuddy.food_buddy_api.domain.model.enums.ProductGroup;
import com.foodbuddy.food_buddy_api.domain.model.enums.Unit;
import com.foodbuddy.food_buddy_api.domain.model.valueobject.Barcode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO zur Ausgabe eines Artikels mit allen zugehörigen Informationen.
 *
 * Beinhaltet Verfallsstatus, Lagerinformationen, Nährwerte und Barcode.
 */
@Getter
@Setter
public class ItemResponseDTO {

    private Long id;
    private String name;
    private String brand;
    private Barcode barcode;
    private String category;

    private double quantityValue;
    private Unit quantityUnit;

    private List<ExpirationDTO> expirations;

    private NutritionInfoDTO nutritionInfo;

    private StorageResponseDTO storage;

    private ProductGroup productGroup;
    private String expirationStatus;

}
