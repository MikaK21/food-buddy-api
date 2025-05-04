package com.foodbuddy.food_buddy_api.application.service;

import com.foodbuddy.food_buddy_api.domain.model.Community;
import com.foodbuddy.food_buddy_api.domain.model.MyUser;
import com.foodbuddy.food_buddy_api.domain.model.Storage;
import com.foodbuddy.food_buddy_api.domain.repository.CommunityRepository;
import com.foodbuddy.food_buddy_api.domain.repository.MyUserRepository;
import com.foodbuddy.food_buddy_api.domain.repository.StorageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageService {

    private final StorageRepository storageRepository;
    private final CommunityRepository communityRepository;
    private final MyUserRepository userRepository;

    public StorageService(StorageRepository storageRepository, CommunityRepository communityRepository, MyUserRepository userRepository) {
        this.storageRepository = storageRepository;
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Storage createStorage(Long communityId, String storageName, String username) {
        Community community = getCommunityOrThrow(communityId);
        MyUser user = getUserOrThrow(username);

        if (!community.hasMember(user)) {
            throw new RuntimeException("Only members of the community can create storages.");
        }

        Storage storage = new Storage();
        storage.setName(storageName);
        storage.setCommunity(community);

        return storageRepository.save(storage);
    }

    @Transactional
    public void deleteStorage(Long storageId, String username) {
        Storage storage = getStorageOrThrow(storageId);
        Community community = storage.getCommunity();
        MyUser user = getUserOrThrow(username);

        if (!community.hasMember(user)) {
            throw new RuntimeException("Only members of the community can delete storages.");
        }

        storageRepository.delete(storage);
    }

    @Transactional
    public Storage renameStorage(Long storageId, String newName, String username) {
        Storage storage = getStorageOrThrow(storageId);
        Community community = storage.getCommunity();
        MyUser user = getUserOrThrow(username);

        if (!community.hasMember(user)) {
            throw new RuntimeException("Only members of the community can rename storages.");
        }

        storage.setName(newName);
        return storageRepository.save(storage);
    }

    public List<Storage> getStoragesForCommunity(Long communityId, String username) {
        Community community = getCommunityOrThrow(communityId);
        MyUser user = getUserOrThrow(username);

        if (!community.hasMember(user)) {
            throw new RuntimeException("Only members of the community can view storages.");
        }

        return storageRepository.findByCommunityId(communityId);
    }

    // Helper methods
    private Community getCommunityOrThrow(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Community not found"));
    }

    private MyUser getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    private Storage getStorageOrThrow(Long id) {
        return storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storage not found"));
    }
}
