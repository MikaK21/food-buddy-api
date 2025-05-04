package com.foodbuddy.food_buddy_api.application.helper;

import com.foodbuddy.food_buddy_api.domain.model.*;
import com.foodbuddy.food_buddy_api.domain.repository.*;
import org.springframework.stereotype.Component;

@Component
public class DomainLookupService {

    private final MyUserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final StorageRepository storageRepository;
    private final ShoppingListRepository listRepository;
    private final ShopRepository shopRepository;
    private final ItemRepository itemRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;


    public DomainLookupService(MyUserRepository userRepository,
                               CommunityRepository communityRepository,
                               ItemRepository itemRepository,
                               StorageRepository storageRepository,
                               ShoppingListRepository listRepository,
                               ShopRepository shopRepository,
                               ShoppingListItemRepository shoppingListItemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.communityRepository = communityRepository;
        this.storageRepository = storageRepository;
        this.listRepository = listRepository;
        this.shopRepository = shopRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
    }

    public MyUser getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public Storage getStorageOrThrow(Long id) {
        return storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storage not found with ID: " + id));
    }

    public Item getItemOrThrow(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + id));
    }

    public Community getCommunityOrThrow(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Community not found with ID: " + id));
    }

    public ShoppingList getShoppingListOrThrow(Long id) {
        return listRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShoppingList not found"));
    }

    public Shop getShopOrThrow(Long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
    }

    public ShoppingListItem getShoppingListItemOrThrow(Long id) {
        return shoppingListItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public void checkShopOwnershipOrThrow(Shop shop, String username) {
        if (!shop.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Not allowed to modify this shop.");
        }
    }
}

