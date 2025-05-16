package com.foodbuddy.food_buddy_api.application.helper;

import com.foodbuddy.food_buddy_api.domain.exception.*;
import com.foodbuddy.food_buddy_api.domain.model.*;
import com.foodbuddy.food_buddy_api.domain.repository.*;
import org.springframework.stereotype.Component;

/**
 * Service zur zentralisierten Auflösung von Domain-Entitäten anhand ihrer IDs.
 *
 * - Kapselt Zugriffslogik auf Repositories
 * - Wirft sprechende Exceptions bei nicht gefundenen Entitäten
 * - Enthält Prüfungen für Eigentumsrechte (z. B. bei Shops)
 */
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
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public Storage getStorageOrThrow(Long id) {
        return storageRepository.findById(id)
                .orElseThrow(() -> new StorageNotFoundException(id));
    }

    public Item getItemOrThrow(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    public Community getCommunityOrThrow(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new CommunityNotFoundException(id));
    }

    public ShoppingList getShoppingListOrThrow(Long id) {
        return listRepository.findById(id)
                .orElseThrow(() -> new ShoppingListNotFoundException(id));
    }

    public Shop getShopOrThrow(Long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new ShopNotFoundException(id));
    }

    public ShoppingListItem getShoppingListItemOrThrow(Long id) {
        return shoppingListItemRepository.findById(id)
                .orElseThrow(() -> new ShoppingListItemNotFoundException(id));
    }

    public void checkShopOwnershipOrThrow(Shop shop, String username) {
        if (!shop.getOwner().getUsername().equals(username)) {
            throw new ShopPermissionDeniedException(username, shop.getId());
        }
    }
}
