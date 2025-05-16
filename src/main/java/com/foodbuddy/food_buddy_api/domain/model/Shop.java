package com.foodbuddy.food_buddy_api.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity zur Repräsentation eines Einkaufsgeschäfts.
 *
 * Ein Shop gehört einem Benutzer und kann Einkaufslisten-Einträgen zugewiesen werden.
 */
@Getter
@Setter
@Entity
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(optional = false)
    private MyUser owner;
}
