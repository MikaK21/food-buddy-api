package com.foodbuddy.food_buddy_api.domain.repository;

import com.foodbuddy.food_buddy_api.domain.model.ItemUnitLog;
import com.foodbuddy.food_buddy_api.domain.model.valueobject.Barcode;
import org.springframework.data.domain.Pageable; // ✅ KORREKT!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;

public interface ItemUnitLogRepository extends JpaRepository<ItemUnitLog, Long> {

    @Query("SELECT i FROM ItemUnitLog i WHERE i.name = :name AND i.removedAt IS NULL ORDER BY i.addedAt ASC")
    List<ItemUnitLog> findByNameAndRemovedAtIsNullOrderByAddedAtAsc(@Param("name") String name, Pageable pageable);
}
