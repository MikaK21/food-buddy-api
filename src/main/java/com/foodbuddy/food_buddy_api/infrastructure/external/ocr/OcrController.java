package com.foodbuddy.food_buddy_api.infrastructure.external.ocr;

import com.foodbuddy.food_buddy_api.infrastructure.external.ocr.OcrService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/ocr")
public class OcrController {

    private final OcrService ocrService;

    public OcrController(OcrService ocrService) {
        this.ocrService = ocrService;
    }

    @PostMapping(
            value = "/expiration",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> extractExpiration(
            @RequestPart("image") MultipartFile image) throws IOException {
        String date = ocrService.recognizeExpiration(image);
        return ResponseEntity.ok(Collections.singletonMap("expirationDate", date));
    }
}
