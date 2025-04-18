package com.alouzou.sondage.dto;

import com.alouzou.sondage.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.*;


@Data
@Builder
public class UserDTO {

    public interface OnCreate {};
    public interface OnUpdate {};


    private Long id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Le nom d'utilisateur est obligatoire.")
    private String username;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "L'email est obligatoire.")
    @Email(groups = {OnCreate.class, OnUpdate.class}, message = "Format d'email invalide.")
    private String email;

    @NotBlank(groups = OnCreate.class, message = "Le mot de passe est obligatoire.")
    @Size(groups = {OnCreate.class, OnUpdate.class}, min = 8, message = "Le mot de passe doit contenir au moins 8 caractères.")
    @Pattern(groups = {OnCreate.class, OnUpdate.class}, regexp = ".*[A-Z].*", message = "Le mot de passe doit contenir au moins une majuscule.")
    @Pattern(groups = {OnCreate.class, OnUpdate.class}, regexp = ".*\\d.*", message = "Le mot de passe doit contenir au moins un chiffre.")
    @Pattern(groups = {OnCreate.class, OnUpdate.class}, regexp = ".*[!@#$%^&*].*", message = "Le mot de passe doit contenir au moins un caractère spécial (!@#$%^&*).")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public static UserDTO fromEntity(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

    }

    public static User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;

    }

}
