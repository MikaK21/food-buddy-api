package com.foodbuddy.food_buddy_api.application.service;

import com.foodbuddy.food_buddy_api.domain.model.MyUser;
import com.foodbuddy.food_buddy_api.domain.model.Shop;
import com.foodbuddy.food_buddy_api.domain.model.ShoppingList;
import com.foodbuddy.food_buddy_api.domain.model.ShoppingListItem;
import com.foodbuddy.food_buddy_api.domain.repository.MyUserRepository;
import com.foodbuddy.food_buddy_api.domain.repository.ShopRepository;
import com.foodbuddy.food_buddy_api.domain.repository.ShoppingListItemRepository;
import com.foodbuddy.food_buddy_api.domain.repository.ShoppingListRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingListService {

    private final ShoppingListRepository listRepository;
    private final MyUserRepository userRepository;
    private final ShopRepository shopRepository;
    private final ShoppingListItemRepository itemRepository;

    public ShoppingListService(ShoppingListRepository listRepository,
                               MyUserRepository userRepository,
                               ShopRepository shopRepository,
                               ShoppingListItemRepository itemRepository) {
        this.listRepository = listRepository;
        this.userRepository = userRepository;
        this.shopRepository = shopRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public ShoppingList createList(String listName, String username) {
        MyUser user = getUser(username);
        ShoppingList list = new ShoppingList();
        list.setName(listName);
        list.setLeader(user);
        list.getMembers().add(user); // Optional: Leader automatisch Mitglied

        return listRepository.save(list);
    }

    @Transactional
    public void renameList(Long listId, String username, String newName) {
        ShoppingList list = getList(listId);
        MyUser user = getUser(username);

        if (!list.isLeader(user)) {
            throw new RuntimeException("Only the leader can rename the list.");
        }

        list.setName(newName);
        listRepository.save(list);
    }

    @Transactional
    public void deleteList(Long listId, String username) {
        ShoppingList list = getList(listId);
        MyUser user = getUser(username);
        if (!list.isLeader(user)) {
            throw new RuntimeException("Only the leader can delete the list.");
        }
        listRepository.delete(list);
    }

    @Transactional
    public void addMember(Long listId, String username, String newMemberUsername) {
        ShoppingList list = getList(listId);
        MyUser leader = getUser(username);
        MyUser newMember = getUser(newMemberUsername);

        if (!list.isLeader(leader)) {
            throw new RuntimeException("Only the leader can add members.");
        }
        list.addMember(newMember);
        listRepository.save(list);
    }

    @Transactional
    public void removeMember(Long listId, String username, String targetUsername) {
        ShoppingList list = getList(listId);
        MyUser leader = getUser(username);
        MyUser target = getUser(targetUsername);

        if (!list.isLeader(leader)) {
            throw new RuntimeException("Only the leader can remove members.");
        }

        list.removeMember(target);
        listRepository.save(list);
    }

    @Transactional
    public ShoppingListItem addItem(Long listId, String username, String name, int amount, Long shopId) {
        ShoppingList list = getList(listId);
        MyUser user = getUser(username);

        if (!list.hasMember(user)) {
            throw new RuntimeException("Not authorized to modify this list.");
        }

        Shop shop = (shopId != null) ? getShop(shopId) : null;

        ShoppingListItem item = new ShoppingListItem();
        item.setName(name);
        item.setAmount(amount);
        item.setShoppingList(list);
        item.setShop(shop);

        return itemRepository.save(item);
    }

    @Transactional
    public void removeItem(Long itemId, String username) {
        ShoppingListItem item = getItem(itemId);
        ShoppingList list = item.getShoppingList();
        MyUser user = getUser(username);

        if (!list.hasMember(user)) {
            throw new RuntimeException("Not authorized to remove this item.");
        }

        itemRepository.delete(item);
    }

    @Transactional
    public ShoppingListItem updateItem(Long itemId, String username, String name, int amount, Long shopId) {
        ShoppingListItem item = getItem(itemId);
        ShoppingList list = item.getShoppingList();
        MyUser user = getUser(username);

        if (!list.hasMember(user)) {
            throw new RuntimeException("Not authorized to update this item.");
        }

        item.setName(name);
        item.setAmount(amount);
        item.setShop(shopId != null ? getShop(shopId) : null);

        return itemRepository.save(item);
    }

    public List<ShoppingList> getListsForUser(String username) {
        MyUser user = getUser(username);
        return listRepository.findByLeaderOrMembersContains(user, user);
    }

    // Helper methods
    private MyUser getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private ShoppingList getList(Long id) {
        return listRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShoppingList not found"));
    }

    private Shop getShop(Long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
    }

    private ShoppingListItem getItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }
}