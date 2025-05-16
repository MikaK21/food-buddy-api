package com.foodbuddy.food_buddy_api.domain.model.valueobject;

import com.foodbuddy.food_buddy_api.domain.model.enums.Unit;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.Objects;

/**
 * Value Object zur Darstellung einer Mengenangabe mit Einheit.
 *
 * Einheit wird durch das Enum {@code Unit} definiert.
 */
@Embeddable
public class Quantity {

    private double value;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    protected Quantity() {
        //JPA
    }

    public Quantity(double value, Unit unit) {
        validate(value, unit);
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setValue(double value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be greater than 0.");
        }
        this.value = value;
    }

    public void setUnit(Unit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit must not be null.");
        }
        this.unit = unit;
    }

    private void validate(double value, Unit unit) {
        if (value <= 0) throw new IllegalArgumentException("Value must be positive.");
        if (unit == null) throw new IllegalArgumentException("Unit must not be null.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quantity quantity)) return false;
        return Double.compare(quantity.value, value) == 0 && unit == quantity.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }

    @Override
    public String toString() {
        return "Quantity{" + value + " " + unit + '}';
    }
}
