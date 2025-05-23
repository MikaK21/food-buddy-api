package com.foodbuddy.food_buddy_api.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity für einzelne Einträge innerhalb einer Einkaufsliste.
 *
 * Ein Eintrag besteht aus Name, Menge, optionalem Shop und Verknüpfung zur Liste.
 */
@Getter
@Setter
@Entity
public class ShoppingListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int amount;

    @ManyToOne
    private Shop shop;

    @ManyToOne
    private ShoppingList shoppingList;
}
