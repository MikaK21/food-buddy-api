package com.foodbuddy.food_buddy_api.application.service;

import com.foodbuddy.food_buddy_api.application.event.ItemEventPublisher;
import com.foodbuddy.food_buddy_api.application.helper.DomainLookupService;
import com.foodbuddy.food_buddy_api.domain.event.ItemCreatedEvent;
import com.foodbuddy.food_buddy_api.domain.model.*;
import com.foodbuddy.food_buddy_api.domain.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemEventPublisher eventPublisher;
    private final DomainLookupService domainLookupService;

    public ItemService(ItemRepository itemRepository,
                       ItemEventPublisher eventPublisher, DomainLookupService domainLookupService) {
        this.itemRepository = itemRepository;
        this.eventPublisher = eventPublisher;
        this.domainLookupService = domainLookupService;
    }

    @Transactional
    public Item createItem(Long storageId, Item item, String username) {
        Storage storage = domainLookupService.getStorageOrThrow(storageId);
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (!storage.getCommunity().hasMember(user)) {
            throw new RuntimeException("You are not allowed to add items to this storage.");
        }

        item.setStorage(storage);
        Item savedItem = itemRepository.save(item);

        // Event auslösen
        eventPublisher.publish(new ItemCreatedEvent(savedItem));

        return savedItem;
    }

    @Transactional
    public Item updateItem(Long itemId, Item updatedItem, Long newStorageId, String username) {
        Item existingItem = domainLookupService.getItemOrThrow(itemId);
        MyUser user = domainLookupService.getUserOrThrow(username);

        Storage newStorage = domainLookupService.getStorageOrThrow(newStorageId);
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
        Item item = domainLookupService.getItemOrThrow(itemId);
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (!item.getStorage().getCommunity().hasMember(user)) {
            throw new RuntimeException("You are not allowed to delete this item.");
        }

        itemRepository.delete(item);
    }

    public List<Item> getItemsByStorage(Long storageId, String username) {
        Storage storage = domainLookupService.getStorageOrThrow(storageId);
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (!storage.getCommunity().hasMember(user)) {
            throw new RuntimeException("You are not allowed to view items of this storage.");
        }

        return itemRepository.findByStorageId(storageId);
    }
}
