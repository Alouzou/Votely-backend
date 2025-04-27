package com.alouzou.sondage.services;

import com.alouzou.sondage.dto.SurveyDTO;
import com.alouzou.sondage.entities.Question;
import com.alouzou.sondage.entities.Survey;
import com.alouzou.sondage.entities.User;

import java.util.List;
import java.util.Optional;

public interface SurveyService {
    Survey createSurvey(SurveyDTO surveyDto);
    List<Survey> getSurveysByCategory(Long categoryId);
    List<Survey> getSurveysByCreator(Long creatorId);
    Optional<Survey> getSurveyById(Long id);
    List<Survey> findAll();

    void deleteSurvey(Long idSurvey);
}
