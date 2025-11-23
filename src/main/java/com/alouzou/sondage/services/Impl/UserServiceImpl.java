package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.dto.UserDTO;
import com.alouzou.sondage.entities.Role;
import com.alouzou.sondage.entities.RoleName;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.exceptions.ForbiddenActionException;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

//    @Override
//    public User createUser(String username, String email, String password, Set<String> rolesNames) {
//        log.info("Création d'un nouvel utilisateur : {}", email);
//        if (userRepository.findByEmail(email).isPresent()) {
//            throw new ResourceAlreadyUsedException("L'email est déjà utilisé !");
//        }
//
//        if (userRepository.findByUsername(username).isPresent()) {
//            throw new ResourceAlreadyUsedException("Le username est dèja utilisé !");
//        }
//        User user = new User();
//        user.setUsername(username);
//        user.setEmail(email);
//        user.setPassword(passwordEncoder.encode(password));
//
//        Set<Role> roles = new HashSet<>();
//        rolesNames.forEach(roleName -> {
//            try {
//                RoleName enumRoleName = RoleName.valueOf(roleName.toUpperCase());
//                Role role = roleRepository.findByName(enumRoleName)
//                        .orElseThrow(() -> new EntityNotFoundException("Rôle non trouvé : " + roleName));
//                roles.add(role);
//            } catch (IllegalArgumentException e) {
//                throw new EntityNotFoundException("Rôle invalide : " + roleName);
//            }
//        });
//        user.setRoles(roles);
//        return userRepository.save(user);
//    }

    @Override
    public User createUser(UserDTO userDTO) {
        log.info("Création d'un nouvel utilisateur : {}", userDTO.getUsername());
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new ResourceAlreadyUsedException("L'email est déjà utilisé !");
        }

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new ResourceAlreadyUsedException("Le username est dèja utilisé !");
        }

        if(userDTO.getRole() == null || userDTO.getRole().getId() == null){
            throw new IllegalArgumentException("Le rôle est obligatoire");
        }

        Role role = roleRepository.findById(userDTO.getRole().getId())
                .orElseThrow(() -> new EntityNotFoundException("Rôle non trouvé"));

        User user = UserDTO.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(role);
        System.out.println("User recu : " + user);
        log.info("Utilisateur créé avec succès : {}", user.getEmail());
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

        if (!userRepository.existsById(id)) {
            log.warn("Utilisateur avec l'ID {} n'existe pas", id);
            return false;
        }

        User currentUser = authService.getCurrentUser();
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        if (userToDelete.getId().equals(currentUser.getId())) {
            throw new ForbiddenActionException("Vous ne pouvez pas vous supprimer vous-même");
        }

        userRepository.deleteById(id);
        log.info("Utilisateur avec l'ID {} supprimé avec succès", id);
        return true;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }





    @Override
    public User modifyUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        log.info("Modificatoin de l'utilisateur : {}", id);

        User userPrincipal = authService.getCurrentUser();
        if (user.getId().compareTo(userPrincipal.getId()) == 0 || authService.hasRole(userPrincipal, RoleName.ROLE_ADMIN)) {
            if (userDTO.getUsername() != null) {
                if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
                    throw new ResourceAlreadyUsedException("Le username est déjà utilisé !");
                }
                if (userDTO.getUsername().isEmpty()) {
                    throw new IllegalArgumentException("Veuillez entrer le username");
                }
                user.setUsername(userDTO.getUsername());
            }
            if (userDTO.getEmail() != null) {
                if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                    throw new ResourceAlreadyUsedException("L'email est déjà utilisé !");
                }
                user.setEmail(userDTO.getEmail());
            }
            if (userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            return userRepository.save(user);
        }
        throw new ForbiddenActionException("Seuls un administrateur ou l'utilisateur lui-même peuvent effectuer cette modification.");
    }

    @Override
    public Page<UserDTO> listerUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

//        users.forEach(user -> {
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//            System.out.println("User: " + user.getUsername());
//            System.out.println("Roles size: " + user.getRoles().size());
//            user.getRoles().forEach(role ->
//                    System.out.println("Role: " + role.getName())
//            );
//        });

        return users.map(UserDTO::fromEntity);

//        return userRepository.findAll(pageable).map(UserDTO::fromEntity);
    }
}
