package com.foodbuddy.food_buddy_api.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(mappedBy = "shops")
    private Set<MyUser> users = new HashSet<>();
}
