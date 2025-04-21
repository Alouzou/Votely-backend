package com.alouzou.sondage.config;

import com.alouzou.sondage.entities.*;
import com.alouzou.sondage.repositories.CategoryRepository;
import com.alouzou.sondage.repositories.RoleRepository;
import com.alouzou.sondage.services.SurveyService;
import com.alouzou.sondage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

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

         //Ajout des utilisateurs
        if (userService.getUserByEmail("admin@example.com").isEmpty()) {
            userService.createUser("admin", "admin@example.com", "password", Set.of(RoleName.ROLE_ADMIN.name(), RoleName.ROLE_CREATOR.name(), RoleName.ROLE_USER.name()));
            log.info("✅ Admin créé !");
        }

        if (userService.getUserByEmail("user@example.com").isEmpty()) {
            userService.createUser("user1", "user@example.com", "password", Set.of(RoleName.ROLE_USER.name()));
            log.info("✅ Utilisateur créé !");
        }

        if (userService.getUserByEmail("creator@example.com").isEmpty()) {
            userService.createUser("creator1", "creator@example.com", "password", Set.of(RoleName.ROLE_CREATOR.name(), RoleName.ROLE_USER.name()));
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

        //log.info("✅ Catégories ajoutées !");
        //User creator = userService.getUserByEmail("creator@example.com").orElseThrow();
//        Object[][] surveys = {
//                // Titre général, catégorie, questions
//                {
//                        "Sondage sur les langages de programmation",
//                        categoryTech.getId(),
//                        new Question[] {
//                                new Question("Quel est votre langage préféré ?", null),
//                                new Question("Quel langage utilisez-vous au travail ?", null),
//                                new Question("Quel langage souhaitez-vous apprendre ?", null)
//                        }
//                },
//                {
//                        "Sondage sur le sport préféré des gens",
//                        categorySport.getId(),
//                        new Question[] {
//                                new Question("Quel est votre sport préféré ?", null),
//                                new Question("Pratiquez-vous ce sport régulièrement ?", null),
//                                new Question("Quel est le sport que vous aimeriez apprendre ?", null)
//                        }
//                },
//                {
//                        "Sondage sur la gastronomie du monde",
//                        categoryGastronomie.getId(),
//                        new Question[] {
//                                new Question("Quel est votre plat préféré ?", null),
//                                new Question("Aimez-vous cuisiner ?", null),
//                                new Question("Quel type de cuisine aimez-vous ?", null)
//                        }
//                },
//                {
//                        "Sondage sur les destinations de voyage",
//                        categoryVoyage.getId(),
//                        new Question[] {
//                                new Question("Quelle destination aimeriez-vous visiter ?", null),
//                                new Question("Avez-vous déjà visité une destination exotique ?", null),
//                                new Question("Préférez-vous la montagne ou la mer ?", null)
//                        }
//                },
//                {
//                        "Sondage sur les outils technologiques",
//                        categoryTech.getId(),
//                        new Question[] {
//                                new Question("Quel IDE utilisez-vous ?", null),
//                                new Question("Préférez-vous travailler sur frontend ou backend ?", null),
//                                new Question("Quelle technologie trouvez-vous la plus intéressante ?", null)
//                        }
//                },
//                {
//                        "Sondage sur les films",
//                        categoryGastronomie.getId(),
//                        new Question[] {
//                                new Question("Quel genre de film préférez-vous ?", null),
//                                new Question("Quel film avez-vous vu récemment ?", null),
//                                new Question("Avez-vous une recommandation de film ?", null)
//                        }
//                },
//                {
//                        "Sondage sur les préférences musicales",
//                        categorySport.getId(),
//                        new Question[] {
//                                new Question("Quel genre de musique préférez-vous ?", null),
//                                new Question("Assistez-vous à des concerts ?", null),
//                                new Question("Quel est votre artiste préféré ?", null)
//                        }
//                },
//                {
//                        "Sondage sur l'IA et ses implications",
//                        categoryTech.getId(),
//                        new Question[] {
//                                new Question("Quel impact pensez-vous que l'IA aura sur le travail ?", null),
//                                new Question("Avez-vous des inquiétudes concernant l'IA ?", null),
//                                new Question("Pensez-vous que l'IA peut remplacer les emplois humains ?", null)
//                        }
//                },
//                {
//                        "Sondage sur les jeux vidéo",
//                        categorySport.getId(),
//                        new Question[] {
//                                new Question("Quel est votre jeu vidéo préféré ?", null),
//                                new Question("Jouez-vous à des jeux vidéo en ligne ?", null),
//                                new Question("Quel genre de jeu préférez-vous ?", null)
//                        }
//                },
//                {
//                        "Sondage sur les vacances et les loisirs",
//                        categoryVoyage.getId(),
//                        new Question[] {
//                                new Question("Quel type de vacances préférez-vous ?", null),
//                                new Question("Participez-vous à des activités de groupe pendant les vacances ?", null),
//                                new Question("Préférez-vous des vacances actives ou détentes ?", null)
//                        }
//                },
//                {
//                        "Sondage sur les sports extrêmes",
//                        categorySport.getId(),
//                        new Question[] {
//                                new Question("Pratiquez-vous des sports extrêmes ?", null),
//                                new Question("Quel sport extrême aimeriez-vous essayer ?", null),
//                                new Question("Quelles sont vos motivations pour pratiquer des sports extrêmes ?", null)
//                        }
//                },
//                {
//                        "Sondage sur les hobbies populaires",
//                        categoryVoyage.getId(),
//                        new Question[] {
//                                new Question("Quel est votre hobby principal ?", null),
//                                new Question("Quel hobby aimeriez-vous pratiquer ?", null),
//                                new Question("Avez-vous des hobbies en dehors des loisirs classiques ?", null)
//                        }
//                },
//                {
//                        "Sondage sur les apps mobiles",
//                        categoryTech.getId(),
//                        new Question[] {
//                                new Question("Quelle est votre application mobile préférée ?", null),
//                                new Question("Combien d'applications utilisez-vous par jour ?", null),
//                                new Question("Avez-vous une application mobile recommandée ?", null)
//                        }
//                },
//                {
//                        "Sondage sur les cuisines du monde",
//                        categoryGastronomie.getId(),
//                        new Question[] {
//                                new Question("Quel est votre plat préféré dans la cuisine ?", null),
//                                new Question("Préférez-vous cuisiner ou manger à l'extérieur ?", null),
//                                new Question("Quelle cuisine du monde aimeriez-vous découvrir ?", null)
//                        }
//                }
//        };
        // Création de sondages à partir du tableau
//        for (Object[] survey : surveys) {
//            surveyService.createSurvey((String) survey[0], creator, (Long) survey[1], (List<Question>) survey[2]);
//
//            log.info("✅ Sondage créé : {}", survey[0]);
//        }
//        // Ajout des sondages
//        User creator = userService.getUserByEmail("creator@example.com").orElseThrow();
//        surveyService.createSurvey("Quel est votre langage préféré ?", "Sondage sur les langages de programmation", categoryTech.getId(), creator);
//        surveyService.createSurvey("Votre sport favori ?", "Sondage sur le sport préféré des gens", categorySport.getId(), creator);

//        log.info("✅ Sondages créés !");


    }
}
