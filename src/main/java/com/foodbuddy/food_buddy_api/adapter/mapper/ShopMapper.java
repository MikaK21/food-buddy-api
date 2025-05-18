package com.foodbuddy.food_buddy_api.adapter.mapper;

import com.foodbuddy.food_buddy_api.adapter.dto.ShopDTO;
import com.foodbuddy.food_buddy_api.adapter.dto.UserResponseDTO;
import com.foodbuddy.food_buddy_api.domain.model.MyUser;
import com.foodbuddy.food_buddy_api.domain.model.Shop;

import java.util.List;
import java.util.stream.Collectors;

public class ShopMapper {

    public static ShopDTO toDTO(Shop shop) {
        List<UserResponseDTO> users = shop.getUsers().stream()
                .map(ShopMapper::mapUser)
                .collect(Collectors.toList());

        return new ShopDTO(shop.getId(), shop.getName(), users);
    }

    public static UserResponseDTO mapUser(MyUser user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}

