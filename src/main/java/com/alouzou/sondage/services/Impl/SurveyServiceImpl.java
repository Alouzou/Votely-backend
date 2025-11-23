package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.dto.ChoiceDTO;
import com.alouzou.sondage.dto.QuestionDTO;
import com.alouzou.sondage.dto.SurveyDTO;
import com.alouzou.sondage.entities.*;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.exceptions.ForbiddenActionException;
import com.alouzou.sondage.exceptions.ResourceAlreadyUsedException;
import com.alouzou.sondage.repositories.*;
import com.alouzou.sondage.services.SurveyService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SurveyServiceImpl implements SurveyService {
    private static final Logger log = LoggerFactory.getLogger(SurveyServiceImpl.class);

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private AuthService authService;


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Survey createSurvey(SurveyDTO surveyDTO) {

        surveyRepository.findByTitle(surveyDTO.getTitle()).ifPresent(s -> {
            throw new ResourceAlreadyUsedException("Le titre est déjà utilisé. Veuillez choisir un autre titre.");
        });

        User creator = userRepository.findById(surveyDTO.getCreatorId())
                .orElseThrow(() -> new EntityNotFoundException("Créateur non trouvé"));
        log.info("Création d'un sondage par {}", creator.getEmail());

        Category category = categoryRepository.findById(surveyDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Catégorie non trouvée !"));

        Survey survey = new Survey();
        survey.setTitle(surveyDTO.getTitle());
        survey.setCreator(creator);
        survey.setCategory(category);

        List<Question> questions = new ArrayList<>();
        if(surveyDTO.getQuestions() != null && !surveyDTO.getQuestions().isEmpty()){
            for (QuestionDTO questionDTO : surveyDTO.getQuestions()){
                Question question = new Question();
                question.setQuestionText(questionDTO.getQuestionText());
                question.setSurvey(survey);

                List<Choice> choices = new ArrayList<>();
                if (questionDTO.getChoices() != null && !questionDTO.getChoices().isEmpty()) {
                    for(ChoiceDTO choiceDTO: questionDTO.getChoices()){
                        Choice choice = new Choice();
                        choice.setQuestion(question);
                        choice.setChoiceText(choiceDTO.getChoiceText());
                        choices.add(choice);
                    }
                }
                question.setChoices(choices);
                questions.add(question);
            }

        }

        survey.setQuestions(questions);

        Survey savedSurvey = surveyRepository.save(survey);

        log.info("Sondage créé avec succès : ID={}, Titre={}, Questions={}",
                savedSurvey.getId(),
                savedSurvey.getTitle(),
                savedSurvey.getQuestions().size());

        return savedSurvey;
    }

    @Override
    public List<Survey> getSurveysByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie non trouvée !"));

        List<Survey> surveys = surveyRepository.findByCategory(category);

        if (surveys.isEmpty()) {
            throw new EntityNotFoundException("Aucun sondage trouvé pour cette catégorie !");
        }

        return surveys;
    }

    @Override
    public List<Survey> getSurveysByCreator(Long creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("Créateur non trouvé avec l'ID : " + creatorId));

        return surveyRepository.findByCreator(creator);
    }

    @Override
    public Optional<Survey> getSurveyById(Long id) {
        surveyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sondage non trouvé avec l'ID : " + id));
        return surveyRepository.findById(id);
    }

    @Override
    public List<Survey> findAll() {
        return surveyRepository.findAll();
    }

    @Override
    public void deleteSurvey(Long idSurvey) {
        Survey survey = surveyRepository.findById(idSurvey)
                .orElseThrow(() -> new EntityNotFoundException("Sondage non trouvé avec id : " + idSurvey));

        User currentUser = authService.getCurrentUser();

        boolean isAdmin = authService.hasRole(currentUser, RoleName.ROLE_ADMIN);
        boolean isOwner = currentUser.getId().equals(survey.getCreator().getId());

        if(!isOwner && !isAdmin) {
            throw new ForbiddenActionException("Vous ne pouvez supprimer que vos propres sondages");
        }
        surveyRepository.deleteById(idSurvey);
    }

    @Override
    public List<QuestionDTO> getSurveyQuestionsWithUserVotes(Long surveyId) {

        List<Question> questions = questionRepository.findBySurveyId(surveyId);
        return questions.stream()
                .map(QuestionDTO::fromEntityWithVotes)
                .collect(Collectors.toList());
    }

}
