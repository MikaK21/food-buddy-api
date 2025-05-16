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
    public ResponseEntity<?> createShop(@RequestBody Map<String, String> body, Principal principal) {
        Shop shop = shopService.createShop(principal.getName(), body.get("name"));
        return ResponseEntity.ok(Map.of("message", "Shop created", "id", shop.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> renameShop(@PathVariable Long id, @RequestBody Map<String, String> body, Principal principal) {
        shopService.renameShop(id, principal.getName(), body.get("name"));
        return ResponseEntity.ok(Map.of("message", "Shop renamed"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShop(@PathVariable Long id, Principal principal) {
        shopService.deleteShop(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Shop deleted"));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyShops(Principal principal) {
        return ResponseEntity.ok(shopService.getMyShops(principal.getName()));
    }
}

