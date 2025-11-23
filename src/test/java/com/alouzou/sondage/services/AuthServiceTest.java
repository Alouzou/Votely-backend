package com.alouzou.sondage.services;


import com.alouzou.sondage.entities.Role;
import com.alouzou.sondage.entities.RoleName;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.entities.UserPrincipal;
import com.alouzou.sondage.services.Impl.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;



import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {


    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void getCurrentUser_utilisateurAuthentifie_retourneUser() {

        User user = new User();
        user.setId(25L);
        user.setEmail("test@example.com");

        UserPrincipal principal = new UserPrincipal(user);

        TestingAuthenticationToken authentication = new TestingAuthenticationToken(principal, null);
        authentication.setAuthenticated(true);


        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);


        User result = authService.getCurrentUser();

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getCurrentUser_utilisateurNonAuthentifie_throwException() {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.getCurrentUser());
        assertEquals("Utilisateur non authentifié", exception.getMessage());
    }

    @Test
    void getCurrentUser_principalPasUserPrincipal_throwException() {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken("string-principal", null);
        authentication.setAuthenticated(true);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.getCurrentUser());
        assertEquals("Utilisateur non authentifié", exception.getMessage());
    }

    @Test
    void hasRole_utilisateurAvecRole_retourneTrue() {
        Role adminRole = new Role();
        adminRole.setName(RoleName.ROLE_ADMIN);

        User user = new User();
        user.setRole(adminRole);

        boolean result = authService.hasRole(user, RoleName.ROLE_ADMIN);
        assertTrue(result);
    }
    @Test
    void hasRole_utilisateurSansRole_retourneFalse() {
        Role userRole = new Role();
        userRole.setName(RoleName.ROLE_USER);

        User user = new User();
        user.setRole(userRole);


        boolean result = authService.hasRole(user, RoleName.ROLE_ADMIN);
        assertFalse(result);
    }

}
