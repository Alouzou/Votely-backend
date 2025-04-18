package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.dto.UserDTO;
import com.alouzou.sondage.entities.Role;
import com.alouzou.sondage.entities.RoleName;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.exceptions.ResourceAlreadyUsedException;
import com.alouzou.sondage.repositories.RoleRepository;
import com.alouzou.sondage.repositories.UserRepository;
import com.alouzou.sondage.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User createUser(String username, String email, String password, RoleName roleName) {
        log.info("Création d'un nouvel utilisateur : {}", email);
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResourceAlreadyUsedException("L'email est déjà utilisé !");
        }

        if(userRepository.findByUsername(username).isPresent()){
            throw new ResourceAlreadyUsedException("Le username est dèja utilisé !");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        Role role = roleRepository.findByName(roleName).
                orElseThrow(() -> new RuntimeException("Rôle non trouvé !"));
        user.getRoles().add(role);

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteUser(Long id) {
        log.info("Suppression de l'utilisateur avec l'ID : {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("SUCCES - Suppression de l'utilisateur avec l'ID : {}", id);
            return true;
        }
        log.info("ÉCHEC - Suppression de l'utilisateur avec l'ID : {}", id);
        return false;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User modifyUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        log.info("Modificatoin de l'utilisateur : {}", id);

        if (userDTO.getUsername() != null) {
            if (userDTO.getUsername().trim().isEmpty()) {
                throw new IllegalArgumentException("Le username ne peut pas être vide");
            }
            if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
                throw new ResourceAlreadyUsedException("Le username est déjà utilisé !");
            }
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getEmail() != null) {
            if (userDTO.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("L'email ne peut pas être vide.");
            }
            if(userRepository.findByEmail(userDTO.getEmail()).isPresent()){
                throw new ResourceAlreadyUsedException("L'email est déjà utilisé !");
            }
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty()) {
            user.setPassword(userDTO.getPassword());
        }
        return userRepository.save(user);
    }

    @Override
    public Page<UserDTO> listerUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDTO::fromEntity);
    }
}
