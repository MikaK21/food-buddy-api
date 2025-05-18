package com.foodbuddy.food_buddy_api.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity zur Repräsentation eines Benutzers.
 *
 * Enthält Benutzername, E-Mail-Adresse und Passwort.
 * Wird für Authentifizierung und Rechteprüfung verwendet.
 */
@Getter
@Setter
@Entity
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 12, message = "Username must be between 4 and 12 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String password;

    @NotBlank(message = "Email cannot be empty")
    private String email;


    @ManyToMany
    @JoinTable(
            name = "user_shop",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "shop_id")
    )
    private Set<Shop> shops = new HashSet<>();
}
