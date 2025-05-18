package com.foodbuddy.food_buddy_api.infrastructure.external.ocr;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class QuickOcrTest {
    public static void main(String[] args) throws Exception {
        // Native OpenCV-Bibliotheken laden (wichtig!)
        OpenCV.loadLocally();

        // 🔍 Lokales Bild laden (z. B. Dosenscan)
        String inputPath = "C:/Users/mikak/Desktop/test.jpg";
        Mat img = Imgcodecs.imread(inputPath);
        if (img.empty()) {
            System.err.println("Bild konnte nicht geladen werden: " + inputPath);
            return;
        }

        // 📐 Zuschnitt (zentraler Bereich)
        int x = (int) (img.width() * 0.2);
        int y = (int) (img.height() * 0.3);
        int w = (int) (img.width() * 0.6);
        int h = (int) (img.height() * 0.4);
        Mat cropped = new Mat(img, new Rect(x, y, w, h));
        Imgcodecs.imwrite("C:/Users/mikak/Desktop/debug_cropped.jpg", cropped);

        // 🎨 Graustufen
        Mat gray = new Mat();
        Imgproc.cvtColor(cropped, gray, Imgproc.COLOR_BGR2GRAY);

        // 🎚️ Kontrast verbessern
        Imgproc.equalizeHist(gray, gray);
        Imgcodecs.imwrite("C:/Users/mikak/Desktop/debug_gray.jpg", gray);

        // ⚫ Binarisierung mit Otsu
        Mat thresh = new Mat();
        Imgproc.threshold(gray, thresh, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        String threshPath = "C:/Users/mikak/Desktop/debug_thresh.jpg";
        Imgcodecs.imwrite(threshPath, thresh);

        // 🔠 OCR mit Tesseract
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // ggf. anpassen
        tesseract.setLanguage("deu+eng");
        tesseract.setPageSegMode(7); // eine einzelne Textzeile
        tesseract.setOcrEngineMode(1); // nur LSTM OCR Engine

        try {
            String result = tesseract.doOCR(new File(threshPath));
            System.out.println("📄 OCR-Ergebnis:\n" + result);
        } catch (TesseractException e) {
            System.err.println("Fehler bei der Texterkennung:");
            e.printStackTrace();
        }
    }
}
