package com.foodbuddy.food_buddy_api.infrastructure.webtoken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Service zur Erstellung, Validierung und Analyse von JWT-Tokens.
 *
 * Nutzt eine geheime Signatur zur Sicherung der Tokens.
 */
@Service
public class JwtService {

    private static final String SECRET = "7F9BB19C7A755EBFB2E7C2F96DD2C6DF2857886984FC253C5604C2B90E029D68DCEE51251D78717767440F2B5A786FBB55651D20A0D357C05CF8D6C722540978";

    private static final long VALIDITY = TimeUnit.DAYS.toMillis(360);

    public String generateToken(UserDetails userDetails) {
        Map<String, String> claims = new HashMap<>();
        claims.put("iss", "FoodBuddy");

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String extractUsername(String jwt) {
        System.out.println("🔓 Extrahiere Username aus JWT");
        return getClaims(jwt).getSubject();
    }


    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public boolean isTokenValid(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }
}
