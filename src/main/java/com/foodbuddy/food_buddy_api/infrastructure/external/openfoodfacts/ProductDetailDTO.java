package com.foodbuddy.food_buddy_api.infrastructure.external.openfoodfacts;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO zur Darstellung von Produktinformationen aus der OpenFoodFacts-API.
 *
 * Beinhaltet:
 * - Produktname, Marke, Barcode, Kategorie
 * - Menge und Einheit
 * - Nährwertangaben pro 100g
 * - URLs zu Produktbildern (z. B. Nährwertetikett)
 */
@Getter
@Setter
public class ProductDetailDTO {
    private String name;
    private String brand;
    private String barcode;
    private String category;

    private double quantity;
    private String unit;

    private double kcal;
    private double carbohydrates;
    private double sugar;
    private double protein;
    private double fat;
    private double saturatedFat;
    private double salt;
    private double fiber;

    private String productPhoto;
    private String nutritionInfoPhoto;
    private String ingredientsPhoto;
}
