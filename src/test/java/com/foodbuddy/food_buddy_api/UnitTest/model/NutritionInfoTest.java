package com.foodbuddy.food_buddy_api.UnitTest.model;

import com.foodbuddy.food_buddy_api.domain.model.valueobject.NutritionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test für das Value Object {@link NutritionInfo}.
 *
 * Testet:
 * - Korrekte Initialisierung mit gültigen Werten
 * - Validierung negativer Eingabewerte
 */
class NutritionInfoTest {

    @Test
    void shouldCreateValidNutritionInfo() {
        NutritionInfo info = new NutritionInfo(100, 20, 15, 5, 10, 3, 1, 0);

        assertEquals(100, info.getKcal());
        assertEquals(20, info.getCarbohydrates());
        assertEquals(15, info.getSugar());
        assertEquals(5, info.getProtein());
        assertEquals(10, info.getFat());
        assertEquals(3, info.getSaturatedFat());
        assertEquals(1, info.getSalt());
        assertEquals(0, info.getFiber());
    }

    @Test
    void shouldThrowExceptionForNegativeValues() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(-1, 0, 0, 0, 0, 0, 0, 0));
    }
}
