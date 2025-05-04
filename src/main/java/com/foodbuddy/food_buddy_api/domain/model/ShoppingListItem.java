package com.foodbuddy.food_buddy_api.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
