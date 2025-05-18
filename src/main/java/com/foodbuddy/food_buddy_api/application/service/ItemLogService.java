package com.foodbuddy.food_buddy_api.application.service;

import com.foodbuddy.food_buddy_api.domain.enums.RemovalReason;
import com.foodbuddy.food_buddy_api.domain.model.Item;
import com.foodbuddy.food_buddy_api.domain.model.ItemUnitLog;
import com.foodbuddy.food_buddy_api.domain.model.valueobject.ExpirationEntry;
import com.foodbuddy.food_buddy_api.domain.repository.ItemUnitLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemLogService {

    private final ItemUnitLogRepository logRepo;

    public void logCreation(Item item) {
        for (ExpirationEntry exp : item.getExpirations()) {
            for (int i = 0; i < exp.getAmount(); i++) {
                ItemUnitLog log = new ItemUnitLog();
                log.setName(item.getName());
                log.setBrand(item.getBrand());
                log.setBarcode(item.getBarcode());
                log.setCategory(item.getCategory());
                log.setQuantity(item.getQuantity());
                log.setExpirationDate(exp.getExpirationDate());
                log.setAddedAt(LocalDate.now());
                log.setStorage(item.getStorage());
                log.setProductGroup(item.getProductGroup());
                logRepo.save(log);
            }
        }
    }

    public void logRemoval(Item item, int count, RemovalReason reason) {
        Pageable page = PageRequest.of(0, count);
        List<ItemUnitLog> toUpdate = logRepo.findByNameAndRemovedAtIsNullOrderByAddedAtAsc(item.getName(), page);
        for (ItemUnitLog l : toUpdate) {
            l.setRemovedAt(LocalDate.now());
            l.setRemovalReason(reason);
        }
        logRepo.saveAll(toUpdate);
    }

    public void logCreation2(Item item, LocalDate expirationDate, int count) {
        for (int i = 0; i < count; i++) {
            ItemUnitLog log = new ItemUnitLog();
            log.setName(item.getName());
            log.setBrand(item.getBrand());
            log.setBarcode(item.getBarcode());
            log.setCategory(item.getCategory());
            log.setQuantity(item.getQuantity());
            log.setExpirationDate(expirationDate);
            log.setAddedAt(LocalDate.now());
            log.setStorage(item.getStorage());
            log.setProductGroup(item.getProductGroup());
            logRepo.save(log);
        }
    }

}

