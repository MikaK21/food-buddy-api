package com.foodbuddy.food_buddy_api.infrastructure.external.ocr;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProcessingDirProvider {

    /**
     * Gibt den Pfad zu ~/Desktop/ocr-processing zurück und legt das Verzeichnis
     * ggf. an.
     */
    public Path getProcessingDir() throws IOException {
        String userHome = System.getProperty("user.home");
        Path dir = Paths.get(userHome, "Desktop", "ocr-processing");
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        return dir;
    }
}

