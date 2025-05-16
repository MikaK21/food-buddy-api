package com.foodbuddy.food_buddy_api.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity zur Repräsentation eines Lagers innerhalb einer Community.
 *
 * Jeder Storage gehört genau einer Community und enthält Artikel (Items).
 */
@Getter
@Setter
@Entity
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Storage name cannot be empty")
    private String name;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;
}
