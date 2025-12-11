package com.alouzou.sondage.config;

import com.alouzou.sondage.entities.*;
import com.alouzou.sondage.repositories.*;
import com.alouzou.sondage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("🚀 Initialisation des données de démonstration pour Votely...");

        createRoles();

        createDemoUsers();

        Map<String, Category> categories = createCategories();

        //createDemoSurveys(categories);

        //simulateVotes();

        log.info("✅ Données de démonstration chargées avec succès !");
        log.info("📋 Comptes de test disponibles pour les recruteurs :");
        log.info("   🔐 Admin: admin@votely.com / Demo123!");
        log.info("   ✏️ Creator: creator@votely.com / Demo123!");
        log.info("   👤 User: user@votely.com / Demo123!");
    }

    private void createRoles() {
        if (roleRepository.findByName(RoleName.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(RoleName.ROLE_ADMIN));
            roleRepository.save(new Role(RoleName.ROLE_CREATOR));
            roleRepository.save(new Role(RoleName.ROLE_USER));
            log.info("✅ Rôles créés");
        }
    }

    private void createDemoUsers() {
        // Admin
//        if (userService.getUserByEmail("admin@votely.com").isEmpty()) {
//            userService.createUser("Admin Demo", "admin@votely.com", "Demo123!",
//                    Set.of(RoleName.ROLE_ADMIN.name(), RoleName.ROLE_CREATOR.name(), RoleName.ROLE_USER.name()));
//        }
//
//        // Creators
//        if (userService.getUserByEmail("creator@votely.com").isEmpty()) {
//            userService.createUser("Sophie Martin", "creator@votely.com", "Demo123!",
//                    Set.of(RoleName.ROLE_CREATOR.name(), RoleName.ROLE_USER.name()));
//        }
//
//        if (userService.getUserByEmail("tech.creator@votely.com").isEmpty()) {
//            userService.createUser("Alex Thompson", "tech.creator@votely.com", "Demo123!",
//                    Set.of(RoleName.ROLE_CREATOR.name(), RoleName.ROLE_USER.name()));
//        }


        String[][] users = {
                {"Marie Dubois", "user@votely.com"},
                {"Jean Laurent", "user2@votely.com"},
                {"Emma Wilson", "user3@votely.com"},
                {"Lucas Chen", "user4@votely.com"},
                {"Olivia Brown", "user5@votely.com"},
                {"Noah Garcia", "user6@votely.com"}
        };

//        for (String[] userData : users) {
//            if (userService.getUserByEmail(userData[1]).isEmpty()) {
//                userService.createUser(userData[0], userData[1], "Demo123!", Set.of(RoleName.ROLE_USER.name()));
//            }
//        }

        log.info("✅ {} utilisateurs de démonstration créés", users.length + 3);
    }

    private Map<String, Category> createCategories() {
        Map<String, Category> categories = new HashMap<>();

        String[] categoryNames = {
                "Technologie",
                "Sport & Santé",
                "Voyage",
                "Gastronomie",
                "Lifestyle",
                "Divertissement",
                "Éducation",
                "Business",
                "Environnement"
        };

        for (String name : categoryNames) {
            Category category = categoryRepository.findByName(name)
                    .orElseGet(() -> categoryRepository.save(new Category(name, true)));
            categories.put(name, category);
        }

        log.info("✅ {} catégories créées", categories.size());
        return categories;
    }

    private void createDemoSurveys(Map<String, Category> categories) {
        User techCreator = userService.getUserByEmail("tech.creator@votely.com").orElseThrow();
        User generalCreator = userService.getUserByEmail("creator@votely.com").orElseThrow();

        createSurveyWithQuestions(
                "Stack technologique préférée en 2025",
                techCreator,
                categories.get("Technologie"),
                new String[][] {
                        {"Quel framework frontend préférez-vous pour vos projets ?",
                                "Angular 18", "React 19", "Vue.js 3", "Svelte 5", "Next.js 14"},
                        {"Quel langage backend utilisez-vous principalement ?",
                                "Java/Spring Boot", "Node.js/Express", "Python/Django", ".NET Core", "Go/Gin"},
                        {"Votre base de données préférée pour les projets d'entreprise ?",
                                "PostgreSQL", "MySQL", "MongoDB", "Redis", "Oracle DB"}
                }
        );


        createSurveyWithQuestions(
                "Pratiques DevOps et Cloud Computing",
                techCreator,
                categories.get("Technologie"),
                new String[][] {
                        {"Quelle plateforme cloud utilisez-vous en production ?",
                                "Amazon AWS", "Microsoft Azure", "Google Cloud", "DigitalOcean", "VPS personnel (OVH, Hetzner)"},
                        {"Quel outil de containerisation préférez-vous ?",
                                "Docker seul", "Docker + Kubernetes", "Docker Compose", "Podman", "Je n'utilise pas de containers"},
                        {"Comment gérez-vous vos déploiements ?",
                                "CI/CD automatisé (GitHub Actions, GitLab)", "Scripts bash personnalisés", "Déploiement manuel", "Platform as a Service", "Serverless (Vercel, Netlify)"}
                }
        );


        createSurveyWithQuestions(
                "L'IA dans le développement moderne",
                techCreator,
                categories.get("Technologie"),
                new String[][] {
                        {"Utilisez-vous des assistants IA pour coder ?",
                                "GitHub Copilot", "ChatGPT/Claude", "Cursor", "Non, je préfère coder sans IA", "J'utilise plusieurs outils IA"},
                        {"Dans quel domaine l'IA vous aide-t-elle le plus ?",
                                "Génération de code", "Debugging et résolution d'erreurs", "Rédaction de documentation", "Écriture de tests", "Conception d'architecture"},
                        {"Quel est votre avis sur l'IA dans le développement ?",
                                "Indispensable aujourd'hui", "Utile mais pas essentielle", "Je m'en méfie", "Ça va remplacer les développeurs", "C'est juste une mode"}
                }
        );


        createSurveyWithQuestions(
                "Tendances du marché tech au Canada",
                generalCreator,
                categories.get("Business"),
                new String[][] {
                        {"Quel est le facteur le plus important dans le choix d'un emploi ?",
                                "Salaire compétitif", "Technologies modernes", "Culture d'entreprise", "Télétravail flexible", "Opportunités de croissance"},
                        {"Quelle taille d'entreprise préférez-vous ?",
                                "Startup (<50 employés)", "Scale-up (50-200)", "Grande entreprise (200+)", "Freelance/Consultant", "Peu importe"},
                        {"Quel salaire annuel visez-vous (CAD) ?",
                                "60k-80k", "80k-100k", "100k-120k", "120k-150k", "150k+"}
                }
        );


        createSurveyWithQuestions(
                "Équilibre vie professionnelle en tech",
                generalCreator,
                categories.get("Lifestyle"),
                new String[][] {
                        {"Quel mode de travail préférez-vous ?",
                                "100% télétravail", "Hybride (2-3 jours bureau)", "100% au bureau", "Flexible selon les projets", "Digital nomade"},
                        {"Combien d'heures codez-vous par semaine ?",
                                "Moins de 30h", "30-40h", "40-50h", "50-60h", "Plus de 60h"},
                        {"À quel moment êtes-vous le plus productif ?",
                                "Tôt le matin (5h-8h)", "Matinée (8h-12h)", "Après-midi (12h-17h)", "Soirée (17h-22h)", "La nuit (22h+)"}
                }
        );


        createSurveyWithQuestions(
                "Formation et apprentissage continu",
                generalCreator,
                categories.get("Éducation"),
                new String[][] {
                        {"Comment apprenez-vous de nouvelles technologies ?",
                                "Documentation officielle", "YouTube/Tutoriels vidéo", "Udemy/Coursera", "Projets personnels", "Au travail uniquement"},
                        {"Combien investissez-vous dans votre formation par an ?",
                                "0$ (ressources gratuites)", "1-100$", "100-500$", "500-1000$", "1000$+"},
                        {"Quelle certification vous intéresse le plus ?",
                                "AWS Certified", "Google Cloud", "Kubernetes (CKA)", "Scrum/Agile", "Aucune, l'expérience suffit"}
                }
        );


        createSurveyWithQuestions(
                "Gaming et développeurs",
                generalCreator,
                categories.get("Divertissement"),
                new String[][] {
                        {"Jouez-vous aux jeux vidéo pour vous détendre ?",
                                "Oui, tous les jours", "Quelques fois par semaine", "Les weekends seulement", "Rarement", "Jamais"},
                        {"Quel type de jeux préférez-vous ?",
                                "FPS compétitifs", "RPG/Aventure", "Stratégie", "Puzzle/Réflexion", "Jeux mobiles casual"}
                }
        );

        log.info("✅ {} sondages de démonstration créés", 7);
    }

    private void createSurveyWithQuestions(String title, User creator, Category category, String[][] questionsData) {

        if(surveyRepository.findByTitle(title).isPresent()){
            log.info("   Sondage '{}' existe déjà, ignoré", title);
            return;
        }

        Survey survey = new Survey(title, creator, category);
        survey = surveyRepository.save(survey);


        for (String[] questionData : questionsData) {
            Question question = new Question(questionData[0], survey);
            question = questionRepository.save(question);


            for (int i = 1; i < questionData.length; i++) {
                Choice choice = new Choice(questionData[i], question);
                choiceRepository.save(choice);
            }
        }

        log.info("   ✓ Sondage créé: {} ({} questions)", title, questionsData.length);
    }

    private void simulateVotes() {
        log.info("📊 Simulation de votes pour rendre les sondages réalistes...");

        List<User> voters = Arrays.asList(
                userService.getUserByEmail("user@votely.com").orElse(null),
                userService.getUserByEmail("user2@votely.com").orElse(null),
                userService.getUserByEmail("user3@votely.com").orElse(null),
                userService.getUserByEmail("user4@votely.com").orElse(null),
                userService.getUserByEmail("user5@votely.com").orElse(null)
        ).stream().filter(Objects::nonNull).toList();

        List<Survey> surveys = surveyRepository.findAll().stream()
                .filter(s -> s.getCategory().getName().equals("Technologie"))
                .limit(3)
                .toList();

        if (voters.isEmpty()) {
            log.warn("Aucun utilisateur trouvé pour simuler les votes");
            return;
        }

        Random random = new Random();
        int totalVotes = 0;

        for (Survey survey : surveys) {
            for (Question question : survey.getQuestions()) {
                List<Choice> choices = question.getChoices();
                if (!choices.isEmpty()) {

                    int[] voteDistribution = generateRealisticDistribution(choices.size(), voters.size());

                    for (int i = 0; i < voters.size() && i < 4; i++) {
                        User voter = voters.get(i);


                        boolean hasVoted = voteRepository.existsByUserIdAndQuestionId(
                                voter.getId(),
                                question.getId()
                        );

                        if (!hasVoted) {
                            int choiceIndex = selectChoiceWithDistribution(voteDistribution, random);
                            Choice selectedChoice = choices.get(Math.min(choiceIndex, choices.size() - 1));

                            Vote vote = Vote.builder()
                                    .user(voter)
                                    .choice(selectedChoice)
                                    .build();

                            voteRepository.save(vote);
                            totalVotes++;
                        }
                    }
                }
            }
        }

        log.info("   ✓ {} votes simulés sur {} sondages", totalVotes, surveys.size());
    }

    private int[] generateRealisticDistribution(int numChoices, int numVoters) {
        int[] distribution = new int[numChoices];

        distribution[0] = 40; // 40% pour le premier choix
        if (numChoices > 1) distribution[1] = 30; // 30% pour le deuxième
        if (numChoices > 2) distribution[2] = 20; // 20% pour le troisième
        for (int i = 3; i < numChoices; i++) {
            distribution[i] = 10 / (numChoices - 3); // Répartir les 10% restants
        }
        return distribution;
    }

    private int selectChoiceWithDistribution(int[] distribution, Random random) {
        int rand = random.nextInt(100);
        int cumulative = 0;
        for (int i = 0; i < distribution.length; i++) {
            cumulative += distribution[i];
            if (rand < cumulative) {
                return i;
            }
        }
        return 0;
    }
}
