package com.alouzou.sondage.services;

import com.alouzou.sondage.dto.UserDTO;
import com.alouzou.sondage.entities.Role;
import com.alouzou.sondage.entities.RoleName;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.exceptions.ForbiddenActionException;
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

import java.util.List;
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

    @Test
    void getUserByEmail_emailExiste_retourneUtilisateur() {
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail(email);

        assertEquals(email, result.get().getEmail());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void getUserByEmail_emailInexistant_retourneVide() {
        when(userRepository.findByEmail("inconnu@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByEmail("inconnu@example.com");

        assertEquals(Optional.empty(), result);
    }

    @Test
    void getAllUsers_retourneListeDesUtilisateurs() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_idExiste_retourneUtilisateur() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(id);

        assertEquals(id, result.get().getId());
    }

    @Test
    void getUserById_idInexistant_retourneVide() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(99L);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void deleteUser_idValide_supprimeUtilisateur() {
        Long id = 1L;
        User user = new User();
        user.setId(id);

        when(userRepository.existsById(id)).thenReturn(true);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User currentUser = new User();
        currentUser.setId(2L); // différent de l'utilisateur à supprimer
        when(authService.getCurrentUser()).thenReturn(currentUser);

        boolean result = userService.deleteUser(id);

        assertEquals(true, result);
        verify(userRepository).deleteById(id);
    }

    @Test
    void deleteUser_idInexistant_retourneFalse() {
        Long id = 99L;
        when(userRepository.existsById(id)).thenReturn(false);

        boolean result = userService.deleteUser(id);

        assertEquals(false, result);
        verify(userRepository, never()).deleteById(id);
    }

    @Test
    void deleteUser_utilisateurSeSupprimeLuiMeme_lanceForbiddenActionException() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        when(userRepository.existsById(id)).thenReturn(true);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        when(authService.getCurrentUser()).thenReturn(user); // même ID

        assertThrows(ForbiddenActionException.class, () -> userService.deleteUser(id));
        verify(userRepository, never()).deleteById(id);
    }

    @Test
    void modifyUser_adminModifieUtilisateur_succes() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("ancien");

        UserDTO dto = new UserDTO();
        dto.setUsername("nouveau");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User admin = new User();
        admin.setId(2L);
        when(authService.getCurrentUser()).thenReturn(admin);
        when(authService.hasRole(admin, RoleName.ROLE_ADMIN)).thenReturn(true);

        when(userRepository.findByUsername("nouveau")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.modifyUser(id, dto);

        assertEquals("nouveau", result.getUsername());
    }

    @Test
    void modifyUser_usernameDejaPris_lanceException() {
        Long id = 1L;
        User user = new User();
        user.setId(id);

        UserDTO dto = new UserDTO();
        dto.setUsername("pris");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(authService.getCurrentUser()).thenReturn(user); // self-update
        when(userRepository.findByUsername("pris")).thenReturn(Optional.of(new User()));

        assertThrows(ResourceAlreadyUsedException.class, () -> userService.modifyUser(id, dto));
    }

    @Test
    void modifyUser_utilisateurNonAutorise_lanceForbiddenActionException() {
        Long id = 25L;
        User user = new User();
        user.setId(id);
        user.setUsername("ancienUsername");

        UserDTO dto = new UserDTO();
        dto.setUsername("nouveauUsername");

        User currentUser = new User();
        currentUser.setId(22L);

        when(userRepository.findById(25L)).thenReturn(Optional.of(user));
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(authService.hasRole(currentUser, RoleName.ROLE_ADMIN)).thenReturn(false);

        assertThrows(ForbiddenActionException.class, () -> userService.modifyUser(id, dto));
    }

}
