package com.foodbuddy.food_buddy_api.domain.model;

import com.foodbuddy.food_buddy_api.domain.model.enums.ItemCategory;
import com.foodbuddy.food_buddy_api.domain.model.enums.ProductGroup;
import com.foodbuddy.food_buddy_api.domain.model.valueobject.Barcode;
import com.foodbuddy.food_buddy_api.domain.model.valueobject.ExpirationEntry;
import com.foodbuddy.food_buddy_api.domain.model.valueobject.NutritionInfo;
import com.foodbuddy.food_buddy_api.domain.model.valueobject.Quantity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity zur Repräsentation eines Lagerartikels.
 *
 * Enthält Name, Marke, Barcode, Kategorie, Menge, Nährwerte, Verfallsdaten und Zuordnung zu einem Speicher (Storage).
 */
@Getter
@Setter
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item name cannot be empty")
    private String name;

    private String brand;

    private Barcode barcode;

    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @Embedded
    private Quantity quantity;

    @ElementCollection
    private List<ExpirationEntry> expirations = new ArrayList<>();

    @Embedded
    private NutritionInfo nutritionInfo;

    @Enumerated(EnumType.STRING)
    private ProductGroup productGroup;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;

    public void addExpiration(int amount, java.time.LocalDate date) {
        expirations.add(new ExpirationEntry(amount, date));
    }
}
