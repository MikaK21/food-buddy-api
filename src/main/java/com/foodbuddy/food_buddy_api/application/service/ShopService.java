package com.foodbuddy.food_buddy_api.application.service;

import com.foodbuddy.food_buddy_api.adapter.dto.ShopDTO;
import com.foodbuddy.food_buddy_api.adapter.mapper.ShopMapper;
import com.foodbuddy.food_buddy_api.application.helper.DomainLookupService;
import com.foodbuddy.food_buddy_api.domain.model.MyUser;
import com.foodbuddy.food_buddy_api.domain.model.Shop;
import com.foodbuddy.food_buddy_api.domain.repository.ShopRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service zur Verwaltung von Shops eines Benutzers.
 *
 * Bietet Funktionen zum Erstellen, Umbenennen, Löschen und Abrufen der eigenen Shops.
 * Prüft Besitzerrechte bei Änderungen.
 */
@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final DomainLookupService domainLookupService;

    public ShopService(ShopRepository shopRepository, DomainLookupService domainLookupService) {
        this.shopRepository = shopRepository;
        this.domainLookupService = domainLookupService;
    }

    @Transactional
    public Shop createOrAssignShop(String username, String name) {
        MyUser user = domainLookupService.getUserOrThrow(username);

        Shop shop = shopRepository.findByName(name).orElseGet(() -> {
            Shop newShop = new Shop();
            newShop.setName(name);
            return shopRepository.save(newShop);
        });

        // Benutzer zu Shop hinzufügen, wenn noch nicht vorhanden
        if (!shop.getUsers().contains(user)) {
            shop.getUsers().add(user);
        }

        // Umgekehrt auch sicherstellen, dass der Shop dem User zugeordnet ist
        if (!user.getShops().contains(shop)) {
            user.getShops().add(shop);
        }

        return shop;
    }

    @Transactional
    public void deleteShopAssignment(Long shopId, String username) {
        MyUser user = domainLookupService.getUserOrThrow(username);
        Shop shop = domainLookupService.getShopOrThrow(shopId);

        if (!shop.getUsers().contains(user)) {
            throw new IllegalStateException("User is not assigned to this shop.");
        }

        shop.getUsers().remove(user);
        user.getShops().remove(shop);

        // Optional: Wenn kein User mehr diesen Shop verwendet, löschen
        if (shop.getUsers().isEmpty()) {
            shopRepository.delete(shop);
        }
    }

    public List<ShopDTO> getMyShops(String username) {
        MyUser user = domainLookupService.getUserOrThrow(username);
        return user.getShops().stream()
                .map(ShopMapper::toDTO)
                .toList();
    }

    public List<ShopDTO> getAllShops() {
        return shopRepository.findAll().stream()
                .map(ShopMapper::toDTO)
                .toList();
    }
}
