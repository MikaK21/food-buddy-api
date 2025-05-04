package com.foodbuddy.food_buddy_api.application.service;

import com.foodbuddy.food_buddy_api.application.helper.DomainLookupService;
import com.foodbuddy.food_buddy_api.domain.model.MyUser;
import com.foodbuddy.food_buddy_api.domain.model.Shop;
import com.foodbuddy.food_buddy_api.domain.repository.ShopRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final DomainLookupService domainLookupService;

    public ShopService(ShopRepository shopRepository, DomainLookupService domainLookupService) {
        this.shopRepository = shopRepository;
        this.domainLookupService = domainLookupService;
    }

    public Shop createShop(String username, String name) {
        MyUser owner = domainLookupService.getUserOrThrow(username);
        Shop shop = new Shop();
        shop.setName(name);
        shop.setOwner(owner);

        return shopRepository.save(shop);
    }

    @Transactional
    public void renameShop(Long shopId, String username, String newName) {
        Shop shop = domainLookupService.getShopOrThrow(shopId);
        domainLookupService.checkShopOwnershipOrThrow(shop, username);
        shop.setName(newName);
    }

    public void deleteShop(Long shopId, String username) {
        Shop shop = domainLookupService.getShopOrThrow(shopId);
        domainLookupService.checkShopOwnershipOrThrow(shop, username);

        shopRepository.delete(shop);
    }

    public List<Shop> getMyShops(String username) {
        return shopRepository.findByOwner(domainLookupService.getUserOrThrow(username));
    }
}

