package com.foodbuddy.food_buddy_api.domain.model;

import com.foodbuddy.food_buddy_api.domain.enums.RemovalReason;
import com.foodbuddy.food_buddy_api.domain.model.enums.ItemCategory;
import com.foodbuddy.food_buddy_api.domain.model.enums.ProductGroup;
import com.foodbuddy.food_buddy_api.domain.model.valueobject.Barcode;
import com.foodbuddy.food_buddy_api.domain.model.valueobject.Quantity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class ItemUnitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;

    @Embedded
    private Barcode barcode;

    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @Embedded
    private Quantity quantity;

    private LocalDate expirationDate;

    private LocalDate addedAt;
    private LocalDate removedAt;

    @Enumerated(EnumType.STRING)
    private RemovalReason removalReason;

    @Enumerated(EnumType.STRING)
    private ProductGroup productGroup;

    @ManyToOne
    private Storage storage;
}
