package com.foodbuddy.food_buddy_api.infrastructure.webtoken;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-Controller zur Benutzer-Authentifizierung.
 *
 * Prüft Zugangsdaten und gibt bei Erfolg ein JWT-Token zurück.
 */
@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody LoginForm loginForm) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.username(), loginForm.password())
        );

        if (!auth.isAuthenticated()) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken((UserDetails) auth.getPrincipal());
    }
}


