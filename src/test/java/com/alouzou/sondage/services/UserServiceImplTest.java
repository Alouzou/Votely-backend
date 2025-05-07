package com.alouzou.sondage.services;

import com.alouzou.sondage.entities.Role;
import com.alouzou.sondage.entities.RoleName;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.exceptions.ResourceAlreadyUsedException;
import com.alouzou.sondage.repositories.RoleRepository;
import com.alouzou.sondage.repositories.UserRepository;
import com.alouzou.sondage.services.Impl.AuthService;
import com.alouzou.sondage.services.Impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserServiceImpl userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Test
    void givenUsernameAndEmail_whenCreatingUser_thenShouldSaveUserWithCorrectUsernameAndEmail() {
        String username = "john";
        String email = "john@example.com";
        Set<String> rolesNames = Set.of("ROLE_USER");

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Role role = new Role();
        role.setName(RoleName.ROLE_ADMIN);

        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = userService.createUser(username, email, "password123", rolesNames);

        assertEquals(email, createdUser.getEmail());
        assertEquals(username, createdUser.getUsername());
    }

    @Test
    void creerUtilisateur_emailDejaUtilise_lanceResourceAlreadyUsedException() {
        String username = "john";
        String email = "john@example.com";
        String password = "motdepasse!!123";
        Set<String> roles = Set.of("ROLE_USER");

        User existingUser = new User();
        existingUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        assertThrows(ResourceAlreadyUsedException.class, () -> {
            userService.createUser(username, email, password, roles);
                }
        );
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void creerUtilisateur_usernameDejaUtilise_lanceResourceAlreadyUsedException() {
        String username = "john";
        String email = "john@example.com";
        String password = "motdepasse!!123";
        Set<String> roles = Set.of("ROLE_USER");

        User existingUser = new User();
        existingUser.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));

        assertThrows(ResourceAlreadyUsedException.class, ()->{
           userService.createUser(username, email, password, roles);
        });
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void creerUtilisateur_roleInexistant_lancerEntityNotFoundException(){
        String username = "john";
        String email = "john@example.com";
        String password = "motdepasse!!123";
        Set<String> roles = Set.of("ROLE_INEXISTANT");

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.createUser(username, email, password, roles);
        });
        verify(userRepository, never()).save(any(User.class));
    }

}
