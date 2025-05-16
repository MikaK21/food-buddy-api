package com.foodbuddy.food_buddy_api.domain.model.valueobject;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Value Object für eine Kombination aus Menge und Verfallsdatum.
 *
 * Wird verwendet, um Artikel mit mehreren Ablaufdaten zu verwalten.
 */
@Embeddable
public class ExpirationEntry {

    private int amount;

    private LocalDate expirationDate;

    protected ExpirationEntry() {
        //JPA
    }

    public ExpirationEntry(int amount, LocalDate expirationDate) {
        validate(amount, expirationDate);
        this.amount = amount;
        this.expirationDate = expirationDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        this.amount = amount;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        if (expirationDate == null) {
            throw new IllegalArgumentException("Expiration date must not be null.");
        }
        this.expirationDate = expirationDate;
    }

    private void validate(int amount, LocalDate expirationDate) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        if (expirationDate == null) throw new IllegalArgumentException("Expiration date must not be null.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpirationEntry that)) return false;
        return amount == that.amount && expirationDate.equals(that.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, expirationDate);
    }

    @Override
    public String toString() {
        return "ExpirationEntry{" +
                "amount=" + amount +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
