package com.foodbuddy.food_buddy_api.infrastructure.external.openfoodfacts;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OpenFoodFactsService {

    private static final String API_URL = "https://world.openfoodfacts.org/api/v0/product/";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProductDetailDTO fetchProductDetails(String barcode) {
        try {
            String url = buildUrl(barcode);
            String response = restTemplate.getForObject(url, String.class);

            validateResponse(response);

            JsonNode productNode = objectMapper.readTree(response).path("product");
            validateProductNode(productNode);

            return mapToProductDetailDto(productNode, barcode);

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Abrufen oder Verarbeiten der Produktdaten: " + e.getMessage());
        }
    }

    private String buildUrl(String barcode) {
        String encoded = URLEncoder.encode(barcode, StandardCharsets.UTF_8);
        return API_URL + encoded + ".json";
    }

    private void validateResponse(String response) {
        if (response == null || response.isEmpty()) {
            throw new RuntimeException("Leere oder ungültige Antwort von Open Food Facts API.");
        }
    }

    private void validateProductNode(JsonNode productNode) {
        if (productNode.isMissingNode()) {
            throw new RuntimeException("Kein 'product' in der API-Antwort gefunden.");
        }
    }

    private ProductDetailDTO mapToProductDetailDto(JsonNode productNode, String barcode) {
        ProductDetailDTO dto = new ProductDetailDTO();

        dto.setName(productNode.path("product_name_de").asText("Unknown"));
        dto.setBrand(productNode.path("brands").asText("Unknown").split(",")[0].trim());
        dto.setBarcode(barcode);
        dto.setCategory(determineCategory(productNode));

        extractQuantityAndUnit(productNode.path("quantity").asText(""), dto);

        JsonNode nutriments = productNode.path("nutriments");
        dto.setKcal(nutriments.path("energy-kcal_100g").asDouble(0));
        dto.setCarbohydrates(nutriments.path("carbohydrates_100g").asDouble(0));
        dto.setSugar(nutriments.path("sugars_100g").asDouble(0));
        dto.setProtein(nutriments.path("proteins_100g").asDouble(0));
        dto.setFat(nutriments.path("fat_100g").asDouble(0));
        dto.setSaturatedFat(nutriments.path("saturated-fat_100g").asDouble(0));
        dto.setSalt(nutriments.path("salt_100g").asDouble(0));
        dto.setFiber(nutriments.path("fiber_100g").asDouble(0));

        dto.setProductPhoto(productNode.path("image_url").asText(null));
        dto.setNutritionInfoPhoto(productNode.path("image_nutrition_url").asText(null));
        dto.setIngredientsPhoto(productNode.path("image_ingredients_url").asText(null));

        return dto;
    }

    private String determineCategory(JsonNode productNode) {
        String categories = productNode.path("categories_tags").toString().toLowerCase();
        return categories.contains("beverages") ? "DRINK" : "FOOD";
    }

    private void extractQuantityAndUnit(String quantityStr, ProductDetailDTO dto) {
        if (quantityStr == null || quantityStr.isBlank()) {
            dto.setQuantity(1.0);
            dto.setUnit("PIECE");
            return;
        }

        quantityStr = quantityStr.replaceAll("\\s+", "")
                .replace(",", ".")
                .toLowerCase();

        Pattern pattern = Pattern.compile("^([0-9]+(?:\\.[0-9]+)?)([a-z]+)$");
        Matcher matcher = pattern.matcher(quantityStr);

        if (matcher.matches()) {
            dto.setQuantity(Double.parseDouble(matcher.group(1)));
            dto.setUnit(mapUnit(matcher.group(2)));
        } else {
            dto.setQuantity(1.0);
            dto.setUnit("PIECE");
        }
    }

    private String mapUnit(String unit) {
        return switch (unit) {
            case "g", "gram", "grams" -> "GRAM";
            case "kg", "kilogram", "kilograms" -> "KILOGRAM";
            case "mg", "milligram", "milligrams" -> "MILLIGRAM";
            case "l", "liter", "liters" -> "LITER";
            case "ml", "milliliter", "milliliters" -> "MILLILITER";
            case "pcs", "piece", "pieces", "stk", "stück" -> "PIECE";
            default -> "PIECE";
        };
    }
}
