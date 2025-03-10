package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.entities.Category;
import com.alouzou.sondage.entities.Survey;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.repositories.CategoryRepository;
import com.alouzou.sondage.repositories.SurveyRepository;
import com.alouzou.sondage.repositories.UserRepository;
import com.alouzou.sondage.services.SurveyService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyServiceImpl implements SurveyService {
    private static final Logger log = LoggerFactory.getLogger(SurveyServiceImpl.class);

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public Survey createSurvey(String title, String description, Long categoryId, User creator) {
        log.info("Création d'un sondage par {}", creator.getEmail());
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée !"));

        Survey survey = new Survey();
        survey.setTitle(title);
        survey.setDescription(description);
        survey.setCategory(category);
        survey.setCreator(creator);


        return surveyRepository.save(survey);
    }

    @Override
    public List<Survey> getSurveysByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée !"));
        return surveyRepository.findByCategory(category);
    }

    @Override
    public List<Survey> getSurveysByCreator(Long creatorId) {
        Optional<User> creator = userRepository.findById(creatorId);

        if(creator.isEmpty()){
            throw new RuntimeException("Créateur non trouvé avec l'ID : " + creatorId);
        }
        return surveyRepository.findByCreator(creator.get());
    }

    @Override
    public Optional<Survey> getSurveyById(Long id) {
        return surveyRepository.findById(id);
    }
}
