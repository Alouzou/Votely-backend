package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.dto.LoginResponse;
import com.alouzou.sondage.dto.UserDTO;
import com.alouzou.sondage.entities.RoleName;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.entities.UserPrincipal;
import com.alouzou.sondage.repositories.UserRepository;
import com.alouzou.sondage.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();
            if(principal instanceof UserPrincipal){
                return ((UserPrincipal) principal).getUser();
            }
        }

        throw new RuntimeException("Utilisateur non authentifié");

    }

    public boolean hasRole(User user, RoleName roleName){
        return user.getRole().getName().equals(roleName);
    }

    public LoginResponse authenticate(String username, String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        String token = jwtUtil.generateToken(username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        UserDTO userDTO = UserDTO.fromEntity(user);
        userDTO.setPassword(null);
        return LoginResponse.builder()
                .token(token)
                .user(userDTO)
                .build();

    }
}
