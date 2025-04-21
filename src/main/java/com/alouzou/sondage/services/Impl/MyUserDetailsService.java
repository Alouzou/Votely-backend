package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.entities.UserPrincipal;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.repositories.UserRepository;
import com.alouzou.sondage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur : " + username +" non trouvé"));
        return new UserPrincipal(user);
    }
}
