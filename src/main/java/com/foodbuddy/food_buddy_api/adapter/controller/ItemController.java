package com.foodbuddy.food_buddy_api.adapter.controller;

import com.foodbuddy.food_buddy_api.adapter.dto.CreateItemRequestDTO;
import com.foodbuddy.food_buddy_api.adapter.dto.ItemResponseDTO;
import com.foodbuddy.food_buddy_api.adapter.mapper.ItemMapper;
import com.foodbuddy.food_buddy_api.domain.model.Item;
import com.foodbuddy.food_buddy_api.application.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * REST-Controller für Artikel (Items) innerhalb eines Speichers (Storage).
 *
 * Bietet Funktionen:
 * - Erstellen eines Artikels mit Mengen- und Nährwertangaben
 * - Aktualisieren eines bestehenden Artikels
 * - Löschen eines Artikels
 * - Auflisten aller Artikel in einem bestimmten Storage
 * - Anzeigen des Verfallsstatus (OK, WARNING, EXPIRED)
 */
@RestController
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody CreateItemRequestDTO dto, Principal principal) {
        Item item = itemMapper.toItem(dto);
        Item created = itemService.createItem(dto.getStorageId(), item, principal.getName());

        return ResponseEntity.ok(Map.of(
                "message", "Item successfully created",
                "id", created.getId()
        ));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable Long itemId, @RequestBody CreateItemRequestDTO dto, Principal principal) {
        Item updatedItem = itemMapper.toItem(dto);
        Item savedItem = itemService.updateItem(itemId, updatedItem, dto.getStorageId(), principal.getName());

        return ResponseEntity.ok(Map.of(
                "message", "Item successfully updated",
                "id", savedItem.getId()
        ));
    }

    @PutMapping("/{itemId}/consume")
    public ResponseEntity<?> consumeItem(
            @PathVariable Long itemId,
            @RequestBody Map<String, String> body,
            Principal principal) {
        String expirationDateStr = body.get("date");
        itemService.consumeItem(itemId, expirationDateStr, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Item marked as consumed"));
    }

    @PutMapping("/{itemId}/discard")
    public ResponseEntity<?> discardItem(
            @PathVariable Long itemId,
            @RequestBody Map<String, String> body,
            Principal principal) {
        String expirationDateStr = body.get("date");
        itemService.discardItem(itemId, expirationDateStr, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Item marked as discarded"));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId, Principal principal) {
        itemService.deleteItem(itemId, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Item successfully deleted"));
    }

    @GetMapping("/storage/{storageId}")
    public ResponseEntity<?> getItemsByStorage(@PathVariable Long storageId, Principal principal) {
        List<Item> items = itemService.getItemsByStorage(storageId, principal.getName());
        List<ItemResponseDTO> result = items.stream()
                .map(itemMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/storage/{storageId}/status")
    public ResponseEntity<?> getItemsWithStatus(@PathVariable Long storageId, Principal principal) {
        List<Item> items = itemService.getItemsByStorage(storageId, principal.getName());
        List<ItemResponseDTO> result = items.stream()
                .map(itemMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<?> getAllItemsForUser(Principal principal) {
        List<Item> items = itemService.getAllItemsForUserCommunities(principal.getName());
        List<ItemResponseDTO> result = items.stream()
                .map(itemMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItemById(@PathVariable Long itemId, Principal principal) {
        Item item = itemService.getItemById(itemId, principal.getName());
        ItemResponseDTO dto = itemMapper.toResponseDTO(item);
        return ResponseEntity.ok(dto);
    }
}
