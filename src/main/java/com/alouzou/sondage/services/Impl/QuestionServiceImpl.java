package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.dto.ChoiceDTO;
import com.alouzou.sondage.dto.QuestionDTO;
import com.alouzou.sondage.entities.*;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.exceptions.ForbiddenActionException;
import com.alouzou.sondage.repositories.QuestionRepository;
import com.alouzou.sondage.repositories.SurveyRepository;
import com.alouzou.sondage.repositories.UserRepository;
import com.alouzou.sondage.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    private AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SurveyRepository surveyRepository;

    @Override
    public Question createQuestion(QuestionDTO questionDto) {
        Survey survey = surveyRepository.findById(questionDto.getSurveyId()).orElseThrow(() -> new EntityNotFoundException("Sondage non trouvé"));
        Question question = new Question();
        question.setQuestionText(questionDto.getQuestionText());
        question.setSurvey(survey);
        Question savedQuestion = questionRepository.save(question);
        savedQuestion.setChoices(
                questionDto.getChoices()
                        .stream()
                        .map(choiceDTO -> ChoiceDTO.toEntity(choiceDTO, savedQuestion))
                        .collect(Collectors.toList()));

        return questionRepository.save(savedQuestion);
    }

    @Override
    public void deleteQuestionById(Long questionId, Long surveyId) {
        Question question = questionRepository.findQuestionById(questionId).orElseThrow(
                () -> new EntityNotFoundException("Question non trouvé !")
        );

        Survey survey = surveyRepository.findSurveyById(surveyId).orElseThrow(
                () -> new EntityNotFoundException("Sondage non trouvé avec l'ID : " + surveyId)
        );

        User currentUser = authService.getCurrentUser();

        boolean isAdmin = authService.hasRole(currentUser, RoleName.ROLE_ADMIN);
        boolean isOwner = currentUser.getId().equals(survey.getCreator().getId());

        if(!isAdmin && !isOwner){
            throw new ForbiddenActionException("Vous ne pouvez supprimer cette question !");
        }

        questionRepository.deleteById(questionId);

    }
}
