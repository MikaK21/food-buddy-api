package com.foodbuddy.food_buddy_api.application.service;

import com.foodbuddy.food_buddy_api.application.event.ItemEventPublisher;
import com.foodbuddy.food_buddy_api.application.helper.DomainLookupService;
import com.foodbuddy.food_buddy_api.domain.enums.RemovalReason;
import com.foodbuddy.food_buddy_api.domain.event.ItemCreatedEvent;
import com.foodbuddy.food_buddy_api.domain.model.*;
import com.foodbuddy.food_buddy_api.domain.model.valueobject.ExpirationEntry;
import com.foodbuddy.food_buddy_api.domain.repository.ItemRepository;
import com.foodbuddy.food_buddy_api.domain.repository.ItemUnitLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemEventPublisher eventPublisher;
    private final DomainLookupService domainLookupService;
    private final ItemLogService itemLogService;
    private final ItemUnitLogRepository logRepo;

    public ItemService(ItemRepository itemRepository,
                       ItemEventPublisher eventPublisher,
                       DomainLookupService domainLookupService,
                       ItemLogService itemLogService,
                       ItemUnitLogRepository logRepo) {
        this.itemRepository = itemRepository;
        this.eventPublisher = eventPublisher;
        this.domainLookupService = domainLookupService;
        this.itemLogService = itemLogService;
        this.logRepo = logRepo;
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

        itemLogService.logCreation(savedItem);
        eventPublisher.publish(new ItemCreatedEvent(savedItem));
        return savedItem;
    }

    @Transactional
    public Item updateItem(Long itemId, Item updatedItem, Long newStorageId, String username) {
        Item existingItem = domainLookupService.getItemOrThrow(itemId);
        MyUser user = domainLookupService.getUserOrThrow(username);
        Storage newStorage = domainLookupService.getStorageOrThrow(newStorageId);

        validateStorageAccess(newStorage, user);

        // Mapping: Ablaufdatum -> Menge
        Map<LocalDate, Integer> oldMap = existingItem.getExpirations().stream()
                .collect(Collectors.toMap(
                        ExpirationEntry::getExpirationDate,
                        ExpirationEntry::getAmount
                ));

        Map<LocalDate, Integer> newMap = updatedItem.getExpirations().stream()
                .collect(Collectors.toMap(
                        ExpirationEntry::getExpirationDate,
                        ExpirationEntry::getAmount
                ));

        applyItemChanges(existingItem, updatedItem, newStorage);

        Item saved = itemRepository.save(existingItem);

        // Vergleiche Mengen je Ablaufdatum
        for (Map.Entry<LocalDate, Integer> entry : newMap.entrySet()) {
            LocalDate date = entry.getKey();
            int newAmount = entry.getValue();
            int oldAmount = oldMap.getOrDefault(date, 0);

            if (newAmount > oldAmount) {
                int diff = newAmount - oldAmount;
                itemLogService.logCreation2(saved, date, diff);
            } else if (newAmount < oldAmount) {
                int diff = oldAmount - newAmount;
                //itemLogService.logRemoval2(saved, date, diff, RemovalReason.REMOVED);
            }
        }

        // Wenn alte Einträge komplett verschwunden sind (gelöscht)
        for (Map.Entry<LocalDate, Integer> entry : oldMap.entrySet()) {
            if (!newMap.containsKey(entry.getKey())) {
                //itemLogService.logRemoval2(saved, entry.getKey(), entry.getValue(), RemovalReason.REMOVED);
                System.out.println("delete");
            }
        }

        return saved;
    }


    public List<Item> getItemsByStorage(Long storageId, String username) {
        Storage storage = domainLookupService.getStorageOrThrow(storageId);
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (!storage.getCommunity().hasMember(user)) {
            throw new RuntimeException("You are not allowed to view items of this storage.");
        }

        return itemRepository.findByStorageId(storageId);
    }

    private void validateStorageAccess(Storage storage, MyUser user) {
        if (!storage.getCommunity().hasMember(user)) {
            throw new RuntimeException("You are not allowed to move item to this storage.");
        }
    }

    private void applyItemChanges(Item existingItem, Item updatedItem, Storage newStorage) {
        existingItem.setName(updatedItem.getName());
        existingItem.setBrand(updatedItem.getBrand());
        existingItem.setBarcode(updatedItem.getBarcode());
        existingItem.setCategory(updatedItem.getCategory());
        existingItem.setQuantity(updatedItem.getQuantity());
        existingItem.setExpirations(updatedItem.getExpirations());
        existingItem.setNutritionInfo(updatedItem.getNutritionInfo());
        existingItem.setStorage(newStorage);
        existingItem.setProductGroup(updatedItem.getProductGroup());
    }

    public List<Item> getAllItemsForUserCommunities(String username) {
        return itemRepository.findAllByUserCommunities(username);
    }

    public Item getItemById(Long itemId, String username) {
        Item item = domainLookupService.getItemOrThrow(itemId);
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (!item.getStorage().getCommunity().hasMember(user)) {
            throw new RuntimeException("You are not allowed to view this item.");
        }

        return item;
    }

    @Transactional
    public void deleteItem(Long itemId, String username) {
        Item item = domainLookupService.getItemOrThrow(itemId);
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (!item.getStorage().getCommunity().hasMember(user)) {
            throw new RuntimeException("You are not allowed to delete this item.");
        }

        int remaining = item.getExpirations().stream().mapToInt(ExpirationEntry::getAmount).sum();
        itemLogService.logRemoval(item, remaining, RemovalReason.REMOVED);
        itemRepository.delete(item);
    }

    @Transactional
    public void consumeItem(Long itemId, String expirationDateStr, String username) {
        updateAmountAndLog(itemId, expirationDateStr, username, RemovalReason.EATEN);
    }

    @Transactional
    public void discardItem(Long itemId, String expirationDateStr, String username) {
        updateAmountAndLog(itemId, expirationDateStr, username, RemovalReason.THROWN_AWAY);
    }

    private void updateAmountAndLog(Long itemId, String expirationDateStr, String username, RemovalReason reason) {
        Item item = domainLookupService.getItemOrThrow(itemId);
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (!item.getStorage().getCommunity().hasMember(user)) {
            throw new RuntimeException("Not authorized");
        }

        LocalDate expDate = LocalDate.parse(expirationDateStr);
        ExpirationEntry entry = item.getExpirations().stream()
                .filter(e -> expDate.equals(e.getExpirationDate()) && e.getAmount() > 0)
                .findFirst().orElseThrow(() -> new RuntimeException("Expiration not found or amount 0"));

        // Sonderfall: Letzte Einheit → Eintrag löschen + ggf. gesamtes Item
        if (entry.getAmount() == 1) {
            itemLogService.logRemoval(item, 1, reason);
            item.getExpirations().remove(entry);

            if (item.getExpirations().isEmpty()) {
                itemRepository.delete(item);
                return;
            }
        } else {
            entry.setAmount(entry.getAmount() - 1);
            itemLogService.logRemoval(item, 1, reason);
            itemRepository.save(item);
        }
    }
}
