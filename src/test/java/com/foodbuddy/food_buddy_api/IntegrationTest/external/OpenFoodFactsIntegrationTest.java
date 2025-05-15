package com.foodbuddy.food_buddy_api.IntegrationTest.external;

import com.foodbuddy.food_buddy_api.infrastructure.external.openfoodfacts.OpenFoodFactsService;
import com.foodbuddy.food_buddy_api.infrastructure.external.openfoodfacts.ProductDetailDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OpenFoodFactsIntegrationTest {

    @Autowired
    private OpenFoodFactsService service;

    @Test
    void fetchProductDetails_realBarcode_returnsValidDto() {
        String barcode = "3017620422003";

        ProductDetailDTO dto = service.fetchProductDetails(barcode);

        assertNotNull(dto);
        assertEquals(barcode, dto.getBarcode());
        assertNotNull(dto.getName());
        assertTrue(dto.getQuantity() > 0);
        assertNotNull(dto.getUnit());

        System.out.println("Produktname: " + dto.getName());
        System.out.println("Marke: " + dto.getBrand());
        System.out.println("Kategorie: " + dto.getCategory());
        System.out.println("Bild-URL: " + dto.getProductPhoto());
    }
}

