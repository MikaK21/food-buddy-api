package com.foodbuddy.food_buddy_api.infrastructure.external.ocr;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class OcrService {
    private static final Logger log = LoggerFactory.getLogger(OcrService.class);

    private final ProcessingDirProvider dirProvider;

    public OcrService(ProcessingDirProvider dirProvider) {
        this.dirProvider = dirProvider;
    }

    public String recognizeExpiration(MultipartFile image) {
        try {
            Path procDir = dirProvider.getProcessingDir();
            String base = "mhd-" + System.currentTimeMillis();

            // Original speichern
            File origFile = procDir.resolve(base + "_0_original.jpg").toFile();
            image.transferTo(origFile);
            log.info("Originalbild gespeichert: {}", origFile.getAbsolutePath());

            // Mat laden
            Mat mat = Imgcodecs.imread(origFile.getAbsolutePath());

            // Zuschneiden auf zentralen Bereich
            int x = (int)(mat.width() * 0.2);
            int y = (int)(mat.height() * 0.3);
            int w = (int)(mat.width() * 0.6);
            int h = (int)(mat.height() * 0.4);
            Rect roi = new Rect(x, y, w, h);
            Mat cropped = new Mat(mat, roi);
            String croppedPath = procDir.resolve(base + "_1_cropped.jpg").toString();
            Imgcodecs.imwrite(croppedPath, cropped);

            // Graustufen
            Mat gray = new Mat();
            Imgproc.cvtColor(cropped, gray, Imgproc.COLOR_BGR2GRAY);
            Imgcodecs.imwrite(procDir.resolve(base + "_2_gray.jpg").toString(), gray);

            // Histogramm-Equalisierung (Kontrast verbessern)
            Mat equalized = new Mat();
            Imgproc.equalizeHist(gray, equalized);
            Imgcodecs.imwrite(procDir.resolve(base + "_3_equalized.jpg").toString(), equalized);

            // Threshold mit Otsu
            Mat thresh = new Mat();
            Imgproc.threshold(equalized, thresh, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
            String threshPath = procDir.resolve(base + "_4_thresh.jpg").toString();
            Imgcodecs.imwrite(threshPath, thresh);

            // OCR mit Tesseract auf dem Threshold-Bild
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // ggf. anpassen
            tesseract.setLanguage("deu+eng");
            tesseract.setPageSegMode(7); // Eine Zeile Text
            tesseract.setOcrEngineMode(1); // Nur LSTM OCR Engine

            String rawText = tesseract.doOCR(new File(threshPath));
            log.info("📝 OCR-Text (roh):\n{}", rawText);

            return rawText;

        } catch (IOException | TesseractException e) {
            log.error("Fehler bei der OCR-Verarbeitung", e);
            throw new RuntimeException(e);
        }
    }
}
