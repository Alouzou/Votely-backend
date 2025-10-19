package com.alouzou.sondage.controllers;


import com.alouzou.sondage.dto.AuthRequest;
import com.alouzou.sondage.dto.LoginResponse;
import com.alouzou.sondage.dto.UserDTO;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.services.Impl.AuthService;
import com.alouzou.sondage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Validated(UserDTO.OnCreate.class) @RequestBody UserDTO userDTO) {
        User createdUser = userService.createUser(
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getRoles()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Utilisateur enregistré avec succès !");
        response.put("user", UserDTO.fromEntity(createdUser));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            LoginResponse response = authService.authenticate(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Identifiants invalides"));
        }
    }




}
