package com.foodbuddy.food_buddy_api.adapter.controller;

import com.foodbuddy.food_buddy_api.domain.model.MyUser;
import com.foodbuddy.food_buddy_api.domain.repository.MyUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegistrationController {

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register/user")
    public ResponseEntity<?> createUser(@Valid @RequestBody MyUser user) {
        if (myUserRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Username already exists"));
        }

        if (myUserRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email already exists"));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        myUserRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User successfully created"));
    }

}