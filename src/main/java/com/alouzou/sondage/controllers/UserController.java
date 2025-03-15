package com.alouzou.sondage.controllers;

import com.alouzou.sondage.dto.UserDTO;
import com.alouzou.sondage.entities.RoleName;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Validated(UserDTO.OnCreate.class) @RequestBody UserDTO userDTO) {
        User createdUser = userService.createUser(
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                RoleName.ROLE_USER
        );
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Utilisateur enregistré avec succès !");
        response.put("user", UserDTO.fromEntity(createdUser));
        return ResponseEntity.ok(response);
    }

    //    @PatchMapping("/modify/{id}")
//    public ResponseEntity<UserDTO> modifyUser(
//            @PathVariable("id") Long id,
//            @Valid @RequestBody Map<String, Object> updates) {
//        User updatedUser = userService.modifyUser(id, updates);
//        return ResponseEntity.ok(UserDTO.fromEntity(updatedUser));
//    }
    @PatchMapping("/modify/{id}")
    public ResponseEntity<UserDTO> modifyUser(
            @PathVariable("id") Long id,
            @Validated(UserDTO.OnUpdate.class) @RequestBody UserDTO userDTO) {
        User updatedUser = userService.modifyUser(id, userDTO);
        return ResponseEntity.ok(UserDTO.fromEntity(updatedUser));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable("userId") Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("userId") Long id) {
        return userService.getUserById(id)
                .map(UserDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
