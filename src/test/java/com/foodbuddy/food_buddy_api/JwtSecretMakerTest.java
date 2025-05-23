package com.foodbuddy.food_buddy_api;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

/**
 * Technischer Test zur Generierung eines JWT Secret Keys.
 *
 * Gibt den Key in hexadezimaler Darstellung aus.
 * Nicht für Produktion oder Unit Tests gedacht.
 */
public class JwtSecretMakerTest {

    @Test
    public void generateSecretKey(){
        SecretKey key = Jwts.SIG.HS512.key().build();
        String encodedKey = DatatypeConverter.printHexBinary(key.getEncoded());
        System.out.printf("\nKey = [%s]\n",encodedKey);
    }
}
