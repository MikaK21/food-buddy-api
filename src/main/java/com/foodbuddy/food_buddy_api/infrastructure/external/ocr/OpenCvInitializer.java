package com.foodbuddy.food_buddy_api.infrastructure.external.ocr;

import jakarta.annotation.PostConstruct;
import nu.pattern.OpenCV;
import org.springframework.stereotype.Component;

@Component
public class OpenCvInitializer {
    @PostConstruct
    public void init() {
        OpenCV.loadLocally();
    }
}

