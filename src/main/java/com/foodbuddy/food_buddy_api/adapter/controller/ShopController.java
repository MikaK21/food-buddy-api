package com.foodbuddy.food_buddy_api.adapter.controller;

import com.foodbuddy.food_buddy_api.domain.model.Shop;
import com.foodbuddy.food_buddy_api.application.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * REST-Controller zur Verwaltung von Einkaufsgeschäften (Shops).
 *
 * Bietet Funktionen:
 * - Erstellen eines neuen Shops
 * - Umbenennen eines Shops
 * - Löschen eines Shops
 * - Auflisten aller Shops des angemeldeten Benutzers
 */
@RestController
@RequestMapping("/api/shop")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping
    public ResponseEntity<?> createOrAssignShop(@RequestBody Map<String, String> body, Principal principal) {
        Shop shop = shopService.createOrAssignShop(principal.getName(), body.get("name"));
        return ResponseEntity.ok(Map.of("message", "Shop created or assigned", "id", shop.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShopAssignment(@PathVariable Long id, Principal principal) {
        shopService.deleteShopAssignment(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Shop unassigned or deleted"));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyShops(Principal principal) {
        return ResponseEntity.ok(shopService.getMyShops(principal.getName()));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllShops() {
        return ResponseEntity.ok(shopService.getAllShops());
    }

}