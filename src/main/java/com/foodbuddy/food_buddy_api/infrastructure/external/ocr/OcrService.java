package com.foodbuddy.food_buddy_api.infrastructure.external.ocr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OcrService {
    private static final Logger log = LoggerFactory.getLogger(OcrService.class);

    // Dynamisch das Projektverzeichnis holen
    private static final String OCR_BASE_PATH = new File("ocr_base").getAbsolutePath();

    public String recognizeExpiration(MultipartFile image) {
        try {
            log.info("🔍 OCR-Basisverzeichnis: {}", OCR_BASE_PATH);

            // 1. Bild in images_det speichern
            File imagesDetDir = new File(OCR_BASE_PATH, "images_det");
            if (!imagesDetDir.exists() && !imagesDetDir.mkdirs()) {
                throw new IOException("❌ Konnte Verzeichnis images_det nicht erstellen");
            }

            File targetFile = new File(imagesDetDir, "input.jpg");
            image.transferTo(targetFile);
            log.info("📷 Bild gespeichert unter: {}", targetFile.getAbsolutePath());

            // 2. run_detection.exe ausführen
            runExecutable("run_detection.exe");

            // 3. run_recognition.exe ausführen
            runExecutable("run_recognition.exe");

            // 4. Ergebnis aus .txt-Datei lesen
            File resultsRecDir = new File(OCR_BASE_PATH, "results_rec");
            File[] txtFiles = resultsRecDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

            if (txtFiles == null || txtFiles.length == 0) {
                throw new FileNotFoundException("❌ Keine .txt-Datei in results_rec gefunden");
            }

            File resultFile = txtFiles[0];
            log.info("📄 Ergebnis-Datei: {}", resultFile.getName());

            StringBuilder result = new StringBuilder();
            try (Scanner scanner = new Scanner(resultFile)) {
                while (scanner.hasNextLine()) {
                    result.append(scanner.nextLine()).append("\n");
                }
            }

            String ocrOutput = result.toString().trim();
            String normalized = normalizeDateWithLabels(ocrOutput);
            return normalized;

        } catch (Exception e) {
            log.error("❌ Fehler bei der Datumserkennung", e);
            throw new RuntimeException("Fehler bei der Datumserkennung", e);
        }
    }

    private void runExecutable(String exeName) throws IOException, InterruptedException {
        File exeFile = new File(OCR_BASE_PATH, exeName);
        if (!exeFile.exists()) {
            throw new FileNotFoundException("❌ Executable nicht gefunden: " + exeName);
        }

        ProcessBuilder pb = new ProcessBuilder(exeFile.getAbsolutePath());
        pb.directory(new File(OCR_BASE_PATH));
        pb.redirectErrorStream(true);

        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.info("[{}] {}", exeName, line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException(exeName + " ist mit Fehlercode " + exitCode + " beendet worden");
        }
    }

    private String normalizeDateWithLabels(String text) {
        try {
            // Regex: Dateiname + Doppelpunkt + Daten + Labels
            Pattern pattern = Pattern.compile("^[^:\\n]+:\\s*([\\w\\s./-]+?)\\s+Labels:\\s*([\\w,\\s]+)$", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                String data = matcher.group(1).trim();
                String labels = matcher.group(2).toLowerCase().trim();

                String[] dataParts = data.split("[\\s./-]+");
                String[] labelParts = labels.split("\\s*,\\s*");

                Map<String, String> dateParts = new HashMap<>();
                for (int i = 0; i < Math.min(dataParts.length, labelParts.length); i++) {
                    dateParts.put(labelParts[i], dataParts[i]);
                }

                // Defaultwerte
                String year = dateParts.getOrDefault("year", "1900");
                String month = dateParts.getOrDefault("month", "01");
                String day = dateParts.getOrDefault("day", "01");

                // Monat konvertieren, falls Text
                if (!month.matches("\\d+")) {
                    try {
                        Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month.toUpperCase());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
                    } catch (ParseException e) {
                        log.warn("⚠️ Ungültiger Monatswert: {}", month);
                        month = "01";
                    }
                }

                year = year.length() == 2 ? "20" + year : year;
                month = String.format("%02d", Integer.parseInt(month));
                day = String.format("%02d", Integer.parseInt(day));

                return String.format("%s-%s-%s", year, month, day);
            }

            return "Kein gültiges Datumsformat mit Labels gefunden";
        } catch (Exception e) {
            log.error("❌ Fehler beim Normalisieren des Datums mit Labels", e);
            return "Fehler beim Normalisieren";
        }
    }

}
