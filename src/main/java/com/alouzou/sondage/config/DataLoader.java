package com.alouzou.sondage.config;

import com.alouzou.sondage.entities.Category;
import com.alouzou.sondage.entities.Role;
import com.alouzou.sondage.entities.RoleName;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.repositories.CategoryRepository;
import com.alouzou.sondage.repositories.RoleRepository;
import com.alouzou.sondage.services.SurveyService;
import com.alouzou.sondage.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SurveyService surveyService;

    @Override
    public void run(String... args) throws Exception {
        log.info("⚡ Initialisation des données de test...");
        // Ajout des rôles
        if (roleRepository.findByName(RoleName.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(RoleName.ROLE_ADMIN));
            roleRepository.save(new Role(RoleName.ROLE_CREATOR));
            roleRepository.save(new Role(RoleName.ROLE_USER));
            log.info("✅ Rôles ajoutés !");
        }

        // Ajout des utilisateurs
        if (userService.getUserByEmail("admin@example.com").isEmpty()) {
            userService.createUser("admin", "admin@example.com", "password", RoleName.ROLE_ADMIN);
            log.info("✅ Admin créé !");
        }

        if (userService.getUserByEmail("user@example.com").isEmpty()) {
            userService.createUser("user1", "user@example.com", "password", RoleName.ROLE_USER);
            log.info("✅ Utilisateur créé !");
        }

        if (userService.getUserByEmail("creator@example.com").isEmpty()) {
            userService.createUser("creator1", "creator@example.com", "password", RoleName.ROLE_CREATOR);
            log.info("✅ Créateur de sondages ajouté !");
        }

        // Ajout des catégories
        Category categoryTech = categoryRepository.findByName("Technologie")
                .orElseGet(() -> categoryRepository.save(new Category("Technologie", true)));
        Category categorySport = categoryRepository.findByName("Sport")
                .orElseGet(() -> categoryRepository.save(new Category("Sport", true)));
        Category categoryVoyage = categoryRepository.findByName("Voyage")
                .orElseGet(() -> categoryRepository.save(new Category("Voyage", true)));
        Category categoryGastronomie = categoryRepository.findByName("Gastronomie")
                .orElseGet(() -> categoryRepository.save(new Category("Gastronomie", true)));

        log.info("✅ Catégories ajoutées !");

        // Ajout des sondages
        User creator = userService.getUserByEmail("creator@example.com").orElseThrow();
        surveyService.createSurvey("Quel est votre langage préféré ?", "Sondage sur les langages de programmation", categoryTech.getId(), creator);
        surveyService.createSurvey("Votre sport favori ?", "Sondage sur le sport préféré des gens", categorySport.getId(), creator);

        log.info("✅ Sondages créés !");


    }
}
