package com.foodbuddy.food_buddy_api.application.service;

import com.foodbuddy.food_buddy_api.application.helper.DomainLookupService;
import com.foodbuddy.food_buddy_api.domain.model.*;
import com.foodbuddy.food_buddy_api.domain.repository.*;
import com.foodbuddy.food_buddy_api.domain.service.ShoppingListDomainService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service zur Geschäftslogik für Einkaufslisten.
 *
 * Verantwortlich für:
 * - Erstellung, Umbenennung und Löschung von Einkaufslisten
 * - Verwaltung von Mitgliedschaften
 * - Verwaltung und Validierung von Items (Duplikate, Limit)
 */
@Service
public class ShoppingListService {

    private final ShoppingListRepository listRepository;
    private final ShoppingListItemRepository itemRepository;
    private final DomainLookupService domainLookupService;
    private final ShoppingListDomainService shoppingListDomainService;

    public ShoppingListService(
            ShoppingListRepository listRepository,
            ShoppingListItemRepository itemRepository,
            DomainLookupService domainLookupService,
            ShoppingListDomainService shoppingListDomainService
    ) {
        this.listRepository = listRepository;
        this.itemRepository = itemRepository;
        this.domainLookupService = domainLookupService;
        this.shoppingListDomainService = shoppingListDomainService;
    }

    @Transactional
    public ShoppingList createList(String listName, String username) {
        MyUser user = domainLookupService.getUserOrThrow(username);
        ShoppingList list = new ShoppingList();
        list.setName(listName);
        list.setLeader(user);
        list.getMembers().add(user);

        return listRepository.save(list);
    }

    @Transactional
    public void renameList(Long listId, String username, String newName) {
        ShoppingList list = domainLookupService.getShoppingListOrThrow(listId);
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (!list.isLeader(user)) {
            throw new RuntimeException("Only the leader can rename the list.");
        }

        list.setName(newName);
        listRepository.save(list);
    }

    @Transactional
    public void deleteList(Long listId, String username) {
        ShoppingList list = domainLookupService.getShoppingListOrThrow(listId);
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (!list.isLeader(user)) {
            throw new RuntimeException("Only the leader can delete the list.");
        }

        listRepository.delete(list);
    }

    @Transactional
    public void addMember(Long listId, String username, String newMemberUsername) {
        ShoppingList list = domainLookupService.getShoppingListOrThrow(listId);
        MyUser leader = domainLookupService.getUserOrThrow(username);
        MyUser newMember = domainLookupService.getUserOrThrow(newMemberUsername);

        if (!list.isLeader(leader)) {
            throw new RuntimeException("Only the leader can add members.");
        }

        list.addMember(newMember);
        listRepository.save(list);
    }

    @Transactional
    public void removeMember(Long listId, String username, String targetUsername) {
        ShoppingList list = domainLookupService.getShoppingListOrThrow(listId);
        MyUser leader = domainLookupService.getUserOrThrow(username);
        MyUser target = domainLookupService.getUserOrThrow(targetUsername);

        if (!list.isLeader(leader)) {
            throw new RuntimeException("Only the leader can remove members.");
        }

        list.removeMember(target);
        listRepository.save(list);
    }

    @Transactional
    public ShoppingListItem addItem(Long listId, String username, String name, int amount, Long shopId) {
        ShoppingList list = domainLookupService.getShoppingListOrThrow(listId);
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (!list.hasMember(user)) {
            throw new RuntimeException("Not authorized to modify this list.");
        }

        if (!shoppingListDomainService.isBelowItemLimit(list)) {
            throw new RuntimeException("Cannot add more than 50 items to the list.");
        }

        Shop shop = (shopId != null) ? domainLookupService.getShopOrThrow(shopId) : null;

        if (shoppingListDomainService.isDuplicateItem(list, name, amount, shop)) {
            throw new RuntimeException("Item already exists in this list.");
        }

        ShoppingListItem item = new ShoppingListItem();
        item.setName(name);
        item.setAmount(amount);
        item.setShoppingList(list);
        item.setShop(shop);

        return itemRepository.save(item);
    }

    @Transactional
    public void removeItem(Long itemId, String username) {
        ShoppingListItem item = domainLookupService.getShoppingListItemOrThrow(itemId);
        ShoppingList list = item.getShoppingList();
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (!list.hasMember(user)) {
            throw new RuntimeException("Not authorized to remove this item.");
        }

        itemRepository.delete(item);
    }

    @Transactional
    public ShoppingListItem updateItem(Long itemId, String username, String name, int amount, Long shopId) {
        ShoppingListItem item = domainLookupService.getShoppingListItemOrThrow(itemId);
        ShoppingList list = item.getShoppingList();
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (!list.hasMember(user)) {
            throw new RuntimeException("Not authorized to update this item.");
        }

        item.setName(name);
        item.setAmount(amount);
        item.setShop(shopId != null ? domainLookupService.getShopOrThrow(shopId) : null);

        return itemRepository.save(item);
    }

    public List<ShoppingList> getListsForUser(String username) {
        MyUser user = domainLookupService.getUserOrThrow(username);
        return listRepository.findByLeaderOrMembersContains(user, user);
    }
}
