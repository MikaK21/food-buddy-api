package com.foodbuddy.food_buddy_api.adapter.mapper;

import com.foodbuddy.food_buddy_api.adapter.dto.*;
import com.foodbuddy.food_buddy_api.domain.model.Item;
import com.foodbuddy.food_buddy_api.domain.model.enums.ItemCategory;
import com.foodbuddy.food_buddy_api.domain.model.enums.ProductGroup;
import com.foodbuddy.food_buddy_api.domain.model.valueobject.*;
import com.foodbuddy.food_buddy_api.domain.service.ItemDomainService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {

    private final ItemDomainService itemDomainService;

    public ItemMapper(ItemDomainService itemDomainService) {
        this.itemDomainService = itemDomainService;
    }

    public Item toItem(CreateItemRequestDTO dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setBrand(dto.getBrand());
        item.setBarcode(dto.getBarcode());
        item.setCategory(ItemCategory.valueOf(dto.getCategory()));
        item.setQuantity(new Quantity(dto.getQuantityValue(), dto.getQuantityUnit()));
        item.setProductGroup(itemDomainService.validateProductGroup(item.getCategory(), item.getProductGroup()));

        if (dto.getExpirations() != null) {
            List<ExpirationEntry> expirations = dto.getExpirations().stream()
                    .map(e -> new ExpirationEntry(e.getAmount(), e.getExpirationDate()))
                    .collect(Collectors.toList());
            item.setExpirations(expirations);
        }

        if (dto.getNutritionInfo() != null) {
            NutritionInfoDTO n = dto.getNutritionInfo();
            item.setNutritionInfo(new NutritionInfo(
                    n.getKcal(), n.getCarbohydrates(), n.getSugar(), n.getProtein(),
                    n.getFat(), n.getSaturatedFat(), n.getSalt(), n.getFiber()
            ));
        }

        return item;
    }

    public ItemResponseDTO toResponseDTO(Item item) {
        ItemResponseDTO dto = new ItemResponseDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setBrand(item.getBrand());
        dto.setBarcode(item.getBarcode());
        dto.setCategory(item.getCategory().name());
        dto.setQuantityValue(item.getQuantity().getValue());
        dto.setQuantityUnit(item.getQuantity().getUnit());
        dto.setProductGroup(item.getProductGroup());

        if (item.getExpirations() != null) {
            dto.setExpirations(item.getExpirations().stream()
                    .map(e -> {
                        ExpirationDTO ex = new ExpirationDTO();
                        ex.setAmount(e.getAmount());
                        ex.setExpirationDate(e.getExpirationDate());
                        return ex;
                    })
                    .collect(Collectors.toList()));
        }

        if (item.getNutritionInfo() != null) {
            NutritionInfo n = item.getNutritionInfo();
            NutritionInfoDTO ni = new NutritionInfoDTO();
            ni.setKcal(n.getKcal());
            ni.setCarbohydrates(n.getCarbohydrates());
            ni.setSugar(n.getSugar());
            ni.setProtein(n.getProtein());
            ni.setFat(n.getFat());
            ni.setSaturatedFat(n.getSaturatedFat());
            ni.setSalt(n.getSalt());
            ni.setFiber(n.getFiber());
            dto.setNutritionInfo(ni);
        }

        if (item.getStorage() != null) {
            dto.setStorage(new StorageResponseDTO(
                    item.getStorage().getId(),
                    item.getStorage().getName()
            ));
        }

        dto.setExpirationStatus(itemDomainService.getExpirationStatus(item, LocalDate.now()).name());

        return dto;
    }
}
