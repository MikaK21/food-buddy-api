package com.foodbuddy.food_buddy_api.adapter.controller;

import com.foodbuddy.food_buddy_api.adapter.dto.StorageResponseDTO;
import com.foodbuddy.food_buddy_api.application.service.StorageService;
import com.foodbuddy.food_buddy_api.domain.model.Storage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * REST-Controller zur Verwaltung von Speichern (Storages) innerhalb einer Community.
 *
 * Bietet Funktionen:
 * - Erstellen eines neuen Speichers
 * - Umbenennen eines Speichers
 * - Löschen eines Speichers
 * - Auflisten aller Speicher innerhalb einer Community
 */
@RestController
@RequestMapping("/api/storage")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    public ResponseEntity<?> createStorage(@RequestBody Map<String, String> body, Principal principal) {
        Long communityId = Long.parseLong(body.get("communityId"));
        String name = body.get("name");

        Storage created = storageService.createStorage(communityId, name, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Storage successfully created",
                "id", created.getId()
        ));
    }

    @PutMapping("/{id}/rename")
    public ResponseEntity<?> renameStorage(@PathVariable Long id, @RequestBody Map<String, String> body, Principal principal) {
        String newName = body.get("name");
        Storage updated = storageService.renameStorage(id, newName, principal.getName());

        return ResponseEntity.ok(Map.of(
                "message", "Storage renamed successfully",
                "id", updated.getId()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStorage(@PathVariable Long id, Principal principal) {
        storageService.deleteStorage(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Storage deleted successfully"));
    }

    @GetMapping("/community/{communityId}")
    public ResponseEntity<?> getStoragesForCommunity(@PathVariable Long communityId, Principal principal) {
        List<Storage> storages = storageService.getStoragesForCommunity(communityId, principal.getName());

        List<StorageResponseDTO> result = storages.stream()
                .map(storage -> new StorageResponseDTO(storage.getId(), storage.getName()))
                .toList();

        return ResponseEntity.ok(result);
    }

}
