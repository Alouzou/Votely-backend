package com.alouzou.sondage.repositories;

import com.alouzou.sondage.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Logger logger = LoggerFactory.getLogger(UserRepository.class);

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    default Optional<User> findUserByEmailWithLog(String email) {
        logger.info("Recherche d'un utilisateur avec l'email : {}", email);
        return findByEmail(email);
    }

    Page<User> findAll(Pageable pageable);
}
