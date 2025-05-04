package com.foodbuddy.food_buddy_api.UnitTest.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    @Test
    void shouldCreateValidQuantity() {
        Quantity quantity = new Quantity(1.5, Unit.LITER);

        assertEquals(1.5, quantity.getValue());
        assertEquals(Unit.LITER, quantity.getUnit());
    }

    @Test
    void shouldThrowExceptionForInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity(-1, Unit.GRAM));
    }

    @Test
    void shouldThrowExceptionForNullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity(1.0, null));
    }
}
