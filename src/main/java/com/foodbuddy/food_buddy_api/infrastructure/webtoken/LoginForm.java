package com.foodbuddy.food_buddy_api.infrastructure.webtoken;

/**
 * Datenträger für Login-Daten (Benutzername und Passwort).
 */
public record LoginForm(String username, String password) {
}
