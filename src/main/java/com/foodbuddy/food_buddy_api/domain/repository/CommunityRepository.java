package com.foodbuddy.food_buddy_api.domain.repository;

import com.foodbuddy.food_buddy_api.domain.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}
