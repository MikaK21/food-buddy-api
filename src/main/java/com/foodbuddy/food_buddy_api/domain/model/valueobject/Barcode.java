package com.foodbuddy.food_buddy_api.domain.model.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value Object zur Darstellung eines Barcodes.
 *
 * Enthält Validierung für numerische Barcodes mit 8–13 Stellen.
 */
@Embeddable
public final class Barcode {
    @Column(name = "barcode")
    private String value;

    public Barcode() {
        // JPA
    }

    public Barcode(String value) {
        if (!value.isEmpty() && !value.matches("\\d{8,13}")) {
            throw new IllegalArgumentException("Invalid barcode");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

