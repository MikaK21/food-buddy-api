package com.foodbuddy.food_buddy_api.domain.repository;

import com.foodbuddy.food_buddy_api.domain.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA Repository für {@code Item}.
 *
 * Bietet zusätzliche Methode zur Abfrage nach Storage-ID.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByStorageId(Long storageId);

    @Query("""
    SELECT i FROM Item i
    WHERE i.storage.community IN (
        SELECT c FROM Community c
        JOIN c.members m
        WHERE m.username = :username
    )
""")
    List<Item> findAllByUserCommunities(@Param("username") String username);

    boolean existsByStorageId(Long storageId);
}
