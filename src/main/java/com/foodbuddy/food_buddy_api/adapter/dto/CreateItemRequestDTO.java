package com.foodbuddy.food_buddy_api.adapter.dto;

import com.foodbuddy.food_buddy_api.domain.model.enums.Unit;
import com.foodbuddy.food_buddy_api.domain.model.enums.ProductGroup;
import com.foodbuddy.food_buddy_api.domain.model.valueobject.Barcode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO für das Erstellen eines neuen Items.
 *
 * Enthält alle erforderlichen Felder zur Initialisierung eines Artikels
 * inklusive Mengenangabe, Barcode, Kategorie, Nährwerte und Verfallsdaten.
 */
@Getter
@Setter
public class CreateItemRequestDTO {

    private Long storageId;

    private String name;
    private String brand;
    private Barcode barcode;
    private String category;

    private double quantityValue;
    private Unit quantityUnit;

    private List<ExpirationDTO> expirations;

    private NutritionInfoDTO nutritionInfo;

    private ProductGroup productGroup;
}
