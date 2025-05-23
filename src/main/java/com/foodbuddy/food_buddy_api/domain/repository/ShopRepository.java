package com.foodbuddy.food_buddy_api.domain.repository;

import com.foodbuddy.food_buddy_api.domain.model.MyUser;
import com.foodbuddy.food_buddy_api.domain.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA Repository für {@code Shop}.
 *
 * Bietet Abfrage nach Shops, die einem bestimmten Benutzer gehören.
 */
public interface ShopRepository extends JpaRepository<Shop, Long> {

    List<Shop> findByOwner(MyUser user);
}
