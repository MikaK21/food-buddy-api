package com.foodbuddy.food_buddy_api.application.service;

import com.foodbuddy.food_buddy_api.adapter.mapper.ItemMapper;
import com.foodbuddy.food_buddy_api.domain.model.*;
import com.foodbuddy.food_buddy_api.domain.repository.CommunityRepository;
import com.foodbuddy.food_buddy_api.domain.repository.ItemRepository;
import com.foodbuddy.food_buddy_api.domain.repository.MyUserRepository;
import com.foodbuddy.food_buddy_api.domain.repository.StorageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final StorageRepository storageRepository;
    private final CommunityRepository communityRepository;
    private final MyUserRepository userRepository;
    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository,
                       StorageRepository storageRepository,
                       CommunityRepository communityRepository,
                       MyUserRepository userRepository,
                       ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.storageRepository = storageRepository;
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
        this.itemMapper = itemMapper;
    }

    @Transactional
    public Item createItem(Long storageId, Item item, String username) {
        Storage storage = getStorageOrThrow(storageId);
        MyUser user = getUserOrThrow(username);

        if (!storage.getCommunity().hasMember(user)) {
            throw new RuntimeException("You are not allowed to add items to this storage.");
        }

        item.setStorage(storage);

        return itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Long itemId, Item updatedItem, Long newStorageId, String username) {
        Item existingItem = getItemOrThrow(itemId);
        MyUser user = getUserOrThrow(username);

        Storage newStorage = getStorageOrThrow(newStorageId);
        if (!newStorage.getCommunity().hasMember(user)) {
            throw new RuntimeException("You are not allowed to move item to this storage.");
        }

        existingItem.setName(updatedItem.getName());
        existingItem.setBrand(updatedItem.getBrand());
        existingItem.setBarcode(updatedItem.getBarcode());
        existingItem.setCategory(updatedItem.getCategory());
        existingItem.setQuantity(updatedItem.getQuantity());
        existingItem.setExpirations(updatedItem.getExpirations());
        existingItem.setNutritionInfo(updatedItem.getNutritionInfo());
        existingItem.setStorage(newStorage);

        return itemRepository.save(existingItem);
    }

    @Transactional
    public void deleteItem(Long itemId, String username) {
        Item item = getItemOrThrow(itemId);
        MyUser user = getUserOrThrow(username);

        if (!item.getStorage().getCommunity().hasMember(user)) {
            throw new RuntimeException("You are not allowed to delete this item.");
        }

        itemRepository.delete(item);
    }

    public List<Item> getItemsByStorage(Long storageId, String username) {
        Storage storage = getStorageOrThrow(storageId);
        MyUser user = getUserOrThrow(username);

        if (!storage.getCommunity().hasMember(user)) {
            throw new RuntimeException("You are not allowed to view items of this storage.");
        }

        return itemRepository.findByStorageId(storageId);
    }

    // 🔹 Helper-Methoden
    private Item getItemOrThrow(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found."));
    }

    private Storage getStorageOrThrow(Long id) {
        return storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storage not found."));
    }

    private MyUser getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }
}
