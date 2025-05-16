package com.foodbuddy.food_buddy_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hauptklasse und Einstiegspunkt der FoodBuddy Spring Boot Anwendung.
 *
 * Startet den eingebetteten Server und initialisiert den Spring-Kontext.
 */
@SpringBootApplication
public class FoodBuddyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodBuddyApiApplication.class, args);
	}

}
