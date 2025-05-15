package com.foodbuddy.food_buddy_api.infrastructure.external.openfoodfacts;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/openfood")
public class OpenFoodFactsController {

    private final OpenFoodFactsService openFoodFactsService;

    public OpenFoodFactsController(OpenFoodFactsService openFoodFactsService) {
        this.openFoodFactsService = openFoodFactsService;
    }

    @PostMapping("/details")
    public ResponseEntity<ProductDetailDTO> getProductDetails(@RequestBody Map<String, String> body) {
        String barcode = body.get("barcode");
        ProductDetailDTO dto = openFoodFactsService.fetchProductDetails(barcode);
        return ResponseEntity.ok(dto);
    }
}

