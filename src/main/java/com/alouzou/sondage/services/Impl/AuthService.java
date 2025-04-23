package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.entities.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
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
}
