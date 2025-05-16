package com.foodbuddy.food_buddy_api.domain.model.valueobject;

import jakarta.persistence.Embeddable;

import java.util.Objects;

/**
 * Value Object zur Speicherung von Nährwertinformationen eines Artikels.
 *
 * Enthält kcal, Kohlenhydrate, Zucker, Eiweiß, Fett, gesättigte Fette, Salz und Ballaststoffe.
 */
@Embeddable
public class NutritionInfo {

    private double kcal;
    private double carbohydrates;
    private double sugar;
    private double protein;
    private double fat;
    private double saturatedFat;
    private double salt;
    private double fiber;

    protected NutritionInfo() {
        //JPA
    }

    public NutritionInfo(double kcal, double carbohydrates, double sugar, double protein,
                         double fat, double saturatedFat, double salt, double fiber) {
        validate(kcal, carbohydrates, sugar, protein, fat, saturatedFat, salt, fiber);

        this.kcal = kcal;
        this.carbohydrates = carbohydrates;
        this.sugar = sugar;
        this.protein = protein;
        this.fat = fat;
        this.saturatedFat = saturatedFat;
        this.salt = salt;
        this.fiber = fiber;
    }

    public double getKcal() { return kcal; }
    public void setKcal(double kcal) {
        if (kcal < 0) throw new IllegalArgumentException("kcal must not be negative.");
        this.kcal = kcal;
    }

    public double getCarbohydrates() { return carbohydrates; }
    public void setCarbohydrates(double carbohydrates) {
        if (carbohydrates < 0) throw new IllegalArgumentException("Carbohydrates must not be negative.");
        this.carbohydrates = carbohydrates;
    }

    public double getSugar() { return sugar; }
    public void setSugar(double sugar) {
        if (sugar < 0) throw new IllegalArgumentException("Sugar must not be negative.");
        this.sugar = sugar;
    }

    public double getProtein() { return protein; }
    public void setProtein(double protein) {
        if (protein < 0) throw new IllegalArgumentException("Protein must not be negative.");
        this.protein = protein;
    }

    public double getFat() { return fat; }
    public void setFat(double fat) {
        if (fat < 0) throw new IllegalArgumentException("Fat must not be negative.");
        this.fat = fat;
    }

    public double getSaturatedFat() { return saturatedFat; }
    public void setSaturatedFat(double saturatedFat) {
        if (saturatedFat < 0) throw new IllegalArgumentException("Saturated fat must not be negative.");
        this.saturatedFat = saturatedFat;
    }

    public double getSalt() { return salt; }
    public void setSalt(double salt) {
        if (salt < 0) throw new IllegalArgumentException("Salt must not be negative.");
        this.salt = salt;
    }

    public double getFiber() { return fiber; }
    public void setFiber(double fiber) {
        if (fiber < 0) throw new IllegalArgumentException("Fiber must not be negative.");
        this.fiber = fiber;
    }

    private void validate(double... values) {
        for (double value : values) {
            if (value < 0) {
                throw new IllegalArgumentException("Nutrition values must not be negative.");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NutritionInfo that)) return false;
        return Double.compare(that.kcal, kcal) == 0 &&
                Double.compare(that.carbohydrates, carbohydrates) == 0 &&
                Double.compare(that.sugar, sugar) == 0 &&
                Double.compare(that.protein, protein) == 0 &&
                Double.compare(that.fat, fat) == 0 &&
                Double.compare(that.saturatedFat, saturatedFat) == 0 &&
                Double.compare(that.salt, salt) == 0 &&
                Double.compare(that.fiber, fiber) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kcal, carbohydrates, sugar, protein, fat, saturatedFat, salt, fiber);
    }

    @Override
    public String toString() {
        return "NutritionInfo{" +
                "kcal=" + kcal +
                ", carbohydrates=" + carbohydrates +
                ", sugar=" + sugar +
                ", protein=" + protein +
                ", fat=" + fat +
                ", saturatedFat=" + saturatedFat +
                ", salt=" + salt +
                ", fiber=" + fiber +
                '}';
    }
}
