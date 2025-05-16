package com.foodbuddy.food_buddy_api.application.service;

import com.foodbuddy.food_buddy_api.application.helper.DomainLookupService;
import com.foodbuddy.food_buddy_api.domain.model.Community;
import com.foodbuddy.food_buddy_api.domain.model.MyUser;
import com.foodbuddy.food_buddy_api.domain.model.Storage;
import com.foodbuddy.food_buddy_api.domain.repository.StorageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service zur Verwaltung von Speichern in einer Community.
 *
 * Ermöglicht Erstellen, Umbenennen, Löschen und Abrufen von Storages.
 * Führt Berechtigungsprüfungen anhand der Community-Mitgliedschaft durch.
 */
@Service
public class StorageService {

    private final StorageRepository storageRepository;
    private final DomainLookupService domainLookupService;

    public StorageService(StorageRepository storageRepository, DomainLookupService domainLookupService) {
        this.storageRepository = storageRepository;
        this.domainLookupService = domainLookupService;
    }

    @Transactional
    public Storage createStorage(Long communityId, String storageName, String username) {
        Community community = domainLookupService.getCommunityOrThrow(communityId);
        MyUser user = domainLookupService.getUserOrThrow(username);

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
        Storage storage = domainLookupService.getStorageOrThrow(storageId);
        Community community = storage.getCommunity();
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (!community.hasMember(user)) {
            throw new RuntimeException("Only members of the community can delete storages.");
        }

        storageRepository.delete(storage);
    }

    @Transactional
    public Storage renameStorage(Long storageId, String newName, String username) {
        Storage storage = domainLookupService.getStorageOrThrow(storageId);
        Community community = storage.getCommunity();
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (!community.hasMember(user)) {
            throw new RuntimeException("Only members of the community can rename storages.");
        }

        storage.setName(newName);
        return storageRepository.save(storage);
    }

    public List<Storage> getStoragesForCommunity(Long communityId, String username) {
        Community community = domainLookupService.getCommunityOrThrow(communityId);
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (!community.hasMember(user)) {
            throw new RuntimeException("Only members of the community can view storages.");
        }

        return storageRepository.findByCommunityId(communityId);
    }
}
