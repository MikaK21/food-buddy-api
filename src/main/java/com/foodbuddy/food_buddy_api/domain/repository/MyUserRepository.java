package com.foodbuddy.food_buddy_api.domain.repository;

import com.foodbuddy.food_buddy_api.domain.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository zur Benutzerverwaltung.
 *
 * Bietet Abfragen nach Benutzername und E-Mail-Adresse.
 */
public interface MyUserRepository extends JpaRepository<MyUser, Long> {

    Optional<MyUser> findByUsername(String username);
    Optional<MyUser> findByEmail(String email);

}
