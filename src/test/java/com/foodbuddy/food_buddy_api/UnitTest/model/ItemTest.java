package com.foodbuddy.food_buddy_api.UnitTest.model;

import com.foodbuddy.food_buddy_api.domain.model.Item;
import com.foodbuddy.food_buddy_api.domain.model.enums.ItemCategory;
import com.foodbuddy.food_buddy_api.domain.model.enums.Unit;
import com.foodbuddy.food_buddy_api.domain.model.valueobject.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void shouldSetAndReturnAllFieldsCorrectly() {
        // Arrange
        Quantity quantity = new Quantity(2.0, Unit.LITER);
        NutritionInfo nutrition = new NutritionInfo(100, 12.5, 10.0, 5.0, 3.0, 1.5, 0.2, 0);
        LocalDate date1 = LocalDate.of(2025, 5, 1);
        LocalDate date2 = LocalDate.of(2025, 6, 1);

        Item item = new Item();
        item.setId(1L);
        item.setName("Joghurt");
        item.setBrand("Müller");
        item.setBarcode("987654321");
        item.setCategory(ItemCategory.FOOD);
        item.setQuantity(quantity);
        item.setNutritionInfo(nutrition);
        item.addExpiration(2, date1);
        item.addExpiration(1, date2);

        // Assert
        assertEquals(1L, item.getId());
        assertEquals("Joghurt", item.getName());
        assertEquals("Müller", item.getBrand());
        assertEquals("987654321", item.getBarcode());
        assertEquals(ItemCategory.FOOD, item.getCategory());

        assertEquals(2.0, item.getQuantity().getValue());
        assertEquals(Unit.LITER, item.getQuantity().getUnit());

        assertEquals(nutrition, item.getNutritionInfo());

        assertEquals(2, item.getExpirations().size());
        assertEquals(date1, item.getExpirations().get(0).getExpirationDate());
        assertEquals(2, item.getExpirations().get(0).getAmount());
        assertEquals(date2, item.getExpirations().get(1).getExpirationDate());
        assertEquals(1, item.getExpirations().get(1).getAmount());
    }
}
