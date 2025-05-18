package com.foodbuddy.food_buddy_api.adapter.controller;

import com.foodbuddy.food_buddy_api.adapter.dto.CommunityResponseDTO;
import com.foodbuddy.food_buddy_api.adapter.dto.StorageResponseDTO;
import com.foodbuddy.food_buddy_api.adapter.dto.UserResponseDTO;
import com.foodbuddy.food_buddy_api.application.service.CommunityService;
import com.foodbuddy.food_buddy_api.domain.model.Community;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * REST-Controller zur Verwaltung von Communities.
 *
 * Bietet Funktionen:
 * - Erstellen einer Community
 * - Hinzufügen und Entfernen von Mitgliedern
 * - Übertragen der Führungsrolle
 * - Verlassen und Löschen einer Community
 * - Auflisten der eigenen Communities
 */
@RestController
@RequestMapping("/api/community")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @PostMapping
    public ResponseEntity<?> createCommunity(@RequestBody Map<String, String> body, Principal principal) {
        String name = body.get("name");
        Community created = communityService.createCommunity(name, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Community successfully created",
                "id", created.getId()
        ));
    }

    @PutMapping("/{id}/rename")
    public ResponseEntity<?> renameCommunity(@PathVariable Long id, @RequestBody Map<String, String> body, Principal principal) {
        String newName = body.get("name");
        communityService.renameCommunity(id, principal.getName(), newName);
        return ResponseEntity.ok(Map.of("message", "Community renamed successfully"));
    }

    @PutMapping("/{id}/add-member")
    public ResponseEntity<?> addMember(@PathVariable Long id, @RequestBody Map<String, String> body, Principal principal) {
        String newMemberUsername = body.get("username");
        communityService.addMember(id, principal.getName(), newMemberUsername);
        return ResponseEntity.ok(Map.of("message", "Member added successfully"));
    }

//    @PutMapping("/{id}/remove-member")
//    public ResponseEntity<?> removeMember(@PathVariable Long id, @RequestBody Map<String, String> body, Principal principal) {
//        String username = body.get("username");
//        communityService.removeMember(id, principal.getName(), username);
//        return ResponseEntity.ok(Map.of("message", "Member removed successfully"));
//    }

    @PutMapping("/{id}/remove-member")
    public ResponseEntity<?> removeMember(@PathVariable Long id, @RequestBody Map<String, String> body, Principal principal) {
        String username = body.get("username");
        String actingUsername = principal.getName();

        System.out.println("🔧 REMOVE-MEMBER API");
        System.out.println("→ acting user (Principal): " + actingUsername);
        System.out.println("→ target username to remove: " + username);

        try {
            communityService.removeMember(id, actingUsername, username);
            return ResponseEntity.ok(Map.of("message", "Member removed successfully"));
        } catch (RuntimeException e) {
            // gib exakte Info an Frontend zurück (zum Debuggen)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    @PutMapping("/{id}/transfer-leader")
    public ResponseEntity<?> transferLeader(@PathVariable Long id, @RequestBody Map<String, String> body, Principal principal) {
        String newLeader = body.get("newLeaderUsername");
        communityService.transferLeadership(id, principal.getName(), newLeader);
        return ResponseEntity.ok(Map.of("message", "Leadership transferred successfully"));
    }

    @PutMapping("/{id}/leave")
    public ResponseEntity<?> leaveCommunity(@PathVariable Long id, Principal principal) {
        communityService.leaveCommunity(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "You have left the community"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommunity(@PathVariable Long id, Principal principal) {
        communityService.deleteCommunity(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Community deleted successfully"));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyCommunities(Principal principal) {
        List<Community> communities = communityService.getCommunitiesForUser(principal.getName());

        List<CommunityResponseDTO> response = communities.stream().map(community ->
                new CommunityResponseDTO(
                        community.getId(),
                        community.getName(),
                        new UserResponseDTO(
                                community.getLeader().getId(),
                                community.getLeader().getUsername(),
                                community.getLeader().getEmail()
                        ),
                        community.getMembers().stream().map(m ->
                                new UserResponseDTO(
                                        m.getId(),
                                        m.getUsername(),
                                        m.getEmail()
                                )
                        ).toList(),
                        community.getStorages().stream().map(s ->
                                new StorageResponseDTO(
                                        s.getId(),
                                        s.getName()
                                )
                        ).toList()
                )
        ).toList();

        return ResponseEntity.ok(response);
    }

}
