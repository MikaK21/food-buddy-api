package com.foodbuddy.food_buddy_api.application.service;

import com.foodbuddy.food_buddy_api.domain.model.MyUser;
import com.foodbuddy.food_buddy_api.domain.model.Shop;
import com.foodbuddy.food_buddy_api.domain.repository.MyUserRepository;
import com.foodbuddy.food_buddy_api.domain.repository.ShopRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final MyUserRepository userRepository;

    public ShopService(ShopRepository shopRepository, MyUserRepository userRepository) {
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
    }

    public Shop createShop(String username, String name) {
        MyUser owner = getUser(username);

        Shop shop = new Shop();
        shop.setName(name);
        shop.setOwner(owner);

        return shopRepository.save(shop);
    }

    @Transactional
    public void renameShop(Long shopId, String username, String newName) {
        Shop shop = getShop(shopId);
        checkOwner(shop, username);
        shop.setName(newName);
    }

    public void deleteShop(Long shopId, String username) {
        Shop shop = getShop(shopId);
        checkOwner(shop, username);

        shopRepository.delete(shop);
    }

    public List<Shop> getMyShops(String username) {
        return shopRepository.findByOwner(getUser(username));
    }

    // Helper methods
    private MyUser getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Shop getShop(Long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
    }

    private void checkOwner(Shop shop, String username) {
        if (!shop.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Not allowed to modify this shop.");
        }
    }
}

