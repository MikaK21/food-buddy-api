package com.foodbuddy.food_buddy_api.adapter.controller;

import com.foodbuddy.food_buddy_api.adapter.dto.UserResponseDTO;
import com.foodbuddy.food_buddy_api.application.service.MyUserDetailService;
import com.foodbuddy.food_buddy_api.domain.model.MyUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class MyUserController {

    private final MyUserDetailService userDetailService;

    public MyUserController(MyUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Principal principal) {
        MyUser user = userDetailService.getDomainUserByUsername(principal.getName());

        return ResponseEntity.ok(new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        ));
    }
}
