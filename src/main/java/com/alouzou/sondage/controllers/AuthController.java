package com.alouzou.sondage.controllers;


import com.alouzou.sondage.dto.AuthRequest;
import com.alouzou.sondage.dto.LoginResponse;
import com.alouzou.sondage.dto.UserDTO;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.services.Impl.AuthService;
import com.alouzou.sondage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Map<String, Object>> register(
            @Validated(UserDTO.OnCreate.class) @RequestBody UserDTO userDTO) {

        User createdUser = userService.createUser(userDTO);

        UserDTO responseDTO = UserDTO.fromEntity(createdUser);
        responseDTO.setPassword(null);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Utilisateur enregistré avec succès !");
        response.put("user", responseDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
            LoginResponse response = authService.authenticate(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(response);

    }




}
