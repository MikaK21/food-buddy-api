package com.foodbuddy.food_buddy_api.UnitTest.model;

import com.foodbuddy.food_buddy_api.domain.model.valueobject.ExpirationEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExpirationEntryTest {

    @Test
    void shouldCreateValidExpirationEntry() {
        LocalDate date = LocalDate.of(2025, 5, 1);
        ExpirationEntry entry = new ExpirationEntry(3, date);

        assertEquals(3, entry.getAmount());
        assertEquals(date, entry.getExpirationDate());
    }

    @Test
    void shouldThrowExceptionForInvalidAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> new ExpirationEntry(0, LocalDate.of(2025, 5, 1)));
    }

    @Test
    void shouldThrowExceptionForNullDate() {
        assertThrows(IllegalArgumentException.class,
                () -> new ExpirationEntry(1, null));
    }
}
