package com.foodbuddy.food_buddy_api.domain.repository;

import com.foodbuddy.food_buddy_api.domain.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA Repository für {@code Item}.
 *
 * Bietet zusätzliche Methode zur Abfrage nach Storage-ID.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByStorageId(Long storageId);
}
