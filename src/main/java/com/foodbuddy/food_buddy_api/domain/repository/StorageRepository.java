package com.foodbuddy.food_buddy_api.domain.repository;

import com.foodbuddy.food_buddy_api.domain.model.Item;
import com.foodbuddy.food_buddy_api.domain.model.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA Repository für {@code Storage}.
 *
 * Bietet Zugriff auf alle Speicher einer bestimmten Community.
 */
public interface StorageRepository extends JpaRepository<Storage, Long> {

    List<Storage> findByCommunityId(Long communityId);

}
