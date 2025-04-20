package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.dto.ChoiceDTO;
import com.alouzou.sondage.dto.QuestionDTO;
import com.alouzou.sondage.entities.Choice;
import com.alouzou.sondage.entities.Question;
import com.alouzou.sondage.entities.Survey;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
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
}
