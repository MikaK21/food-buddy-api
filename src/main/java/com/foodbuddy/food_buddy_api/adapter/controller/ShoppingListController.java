package com.foodbuddy.food_buddy_api.adapter.controller;

import com.foodbuddy.food_buddy_api.adapter.dto.CreateShoppingListDTO;
import com.foodbuddy.food_buddy_api.adapter.dto.ShoppingListResponseDTO;
import com.foodbuddy.food_buddy_api.adapter.mapper.ShoppingListMapper;
import com.foodbuddy.food_buddy_api.domain.model.ShoppingList;
import com.foodbuddy.food_buddy_api.domain.model.ShoppingListItem;
import com.foodbuddy.food_buddy_api.application.service.ShoppingListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shopping-list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @PostMapping
    public ResponseEntity<?> createList(@RequestBody CreateShoppingListDTO dto, Principal principal) {
        ShoppingList created = shoppingListService.createList(dto.getName(), principal.getName());

        return ResponseEntity.ok(Map.of(
                "message", "Shopping list created successfully",
                "id", created.getId()
        ));
    }

    @PutMapping("/{listId}/rename")
    public ResponseEntity<?> renameList(@PathVariable Long listId, @RequestBody Map<String, String> body, Principal principal) {
        shoppingListService.renameList(listId, principal.getName(), body.get("name"));
        return ResponseEntity.ok(Map.of("message", "Shopping list renamed"));
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<?> deleteList(@PathVariable Long listId, Principal principal) {
        shoppingListService.deleteList(listId, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Shopping list deleted"));
    }

    @PutMapping("/{listId}/member")
    public ResponseEntity<?> addMember(@PathVariable Long listId, @RequestBody Map<String, String> body, Principal principal) {
        shoppingListService.addMember(listId, principal.getName(), body.get("username"));
        return ResponseEntity.ok(Map.of("message", "Member added"));
    }

    @DeleteMapping("/{listId}/member")
    public ResponseEntity<?> removeMember(@PathVariable Long listId, @RequestBody Map<String, String> body, Principal principal) {
        shoppingListService.removeMember(listId, principal.getName(), body.get("username"));
        return ResponseEntity.ok(Map.of("message", "Member removed"));
    }

    @PostMapping("/{listId}/item")
    public ResponseEntity<?> addItem(@PathVariable Long listId, @RequestBody Map<String, Object> body, Principal principal) {
        String name = body.get("name").toString();
        int amount = Integer.parseInt(body.get("amount").toString());
        Long shopId = body.get("shopId") != null ? Long.valueOf(body.get("shopId").toString()) : null;

        ShoppingListItem item = shoppingListService.addItem(listId, principal.getName(), name, amount, shopId);
        return ResponseEntity.ok(Map.of("message", "Item added", "id", item.getId()));
    }

    @PutMapping("/item/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable Long itemId, @RequestBody Map<String, Object> body, Principal principal) {
        String name = body.get("name").toString();
        int amount = Integer.parseInt(body.get("amount").toString());
        Long shopId = body.get("shopId") != null ? Long.valueOf(body.get("shopId").toString()) : null;

        ShoppingListItem item = shoppingListService.updateItem(itemId, principal.getName(), name, amount, shopId);
        return ResponseEntity.ok(Map.of("message", "Item updated", "id", item.getId()));
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId, Principal principal) {
        shoppingListService.removeItem(itemId, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Item deleted"));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyLists(Principal principal) {
        List<ShoppingList> lists = shoppingListService.getListsForUser(principal.getName());
        List<ShoppingListResponseDTO> result = lists.stream()
                .map(ShoppingListMapper::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }

}
