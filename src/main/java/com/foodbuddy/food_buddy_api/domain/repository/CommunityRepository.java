package com.foodbuddy.food_buddy_api.domain.repository;

import com.foodbuddy.food_buddy_api.domain.model.Community;
import com.foodbuddy.food_buddy_api.domain.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA Repository für {@code Community}.
 */
public interface CommunityRepository extends JpaRepository<Community, Long> {
    List<Community> findByMembersContaining(MyUser user);
}
