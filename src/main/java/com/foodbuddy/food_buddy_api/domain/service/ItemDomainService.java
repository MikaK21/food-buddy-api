package com.foodbuddy.food_buddy_api.domain.service;

import com.foodbuddy.food_buddy_api.adapter.dto.ExpirationDTO;
import com.foodbuddy.food_buddy_api.domain.model.Item;
import com.foodbuddy.food_buddy_api.domain.model.enums.ExpirationStatus;
import com.foodbuddy.food_buddy_api.domain.model.enums.ItemCategory;
import com.foodbuddy.food_buddy_api.domain.model.enums.ProductGroup;
import com.foodbuddy.food_buddy_api.domain.model.valueobject.ExpirationEntry;
import org.springframework.stereotype.Component;

import java.net.ProtocolFamily;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Fachlogik für Artikel (Items).
 *
 * Bietet:
 * - Regelbasierte Berechnung des Verfallsstatus
 * - Validierung von Produkttyp-Zuordnungen basierend auf Kategorie
 */
@Component
public class ItemDomainService {
    public ExpirationStatus getExpirationStatus(Item item, LocalDate today) {
        long minWarningDays = switch (item.getProductGroup()) {
            case FLEISCHPRODUKT -> 3;
            case MILCHPRODUKT -> 5;
            case GEMUESE, OBST -> 4;
            case KONSERVE -> 14;
            case BACKWARE -> 2;
            default -> 7;
        };

        for (ExpirationEntry e : item.getExpirations()) {
            long days = ChronoUnit.DAYS.between(today, e.getExpirationDate());
            if (days < 0) return ExpirationStatus.EXPIRED;
            if (days <= minWarningDays) return ExpirationStatus.WARNING;
        }

        return ExpirationStatus.OK;
    }

    public ProductGroup validateProductGroup(ItemCategory category, ProductGroup group) {
        if (category != ItemCategory.FOOD) {
            return ProductGroup.SONSTIGES;
        }

        return group != null ? group : ProductGroup.SONSTIGES;
    }

    public ExpirationStatus getStatusForExpiration(ProductGroup group, LocalDate expirationDate, LocalDate today) {
        long days = ChronoUnit.DAYS.between(today, expirationDate);
        long warningDays = switch (group) {
            case FLEISCHPRODUKT -> 3;
            case MILCHPRODUKT -> 5;
            case GEMUESE, OBST -> 4;
            case KONSERVE -> 14;
            case BACKWARE -> 2;
            default -> 7;
        };

        if (days < 0) return ExpirationStatus.EXPIRED;
        if (days <= warningDays) return ExpirationStatus.WARNING;
        return ExpirationStatus.OK;
    }
}
