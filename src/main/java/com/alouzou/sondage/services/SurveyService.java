package com.alouzou.sondage.services;

import com.alouzou.sondage.entities.Survey;
import com.alouzou.sondage.entities.User;

import java.util.List;
import java.util.Optional;

public interface SurveyService {
    Survey createSurvey(String title, String description, Long categoryId, User creator);
    List<Survey> getSurveysByCategory(Long categoryId);
    List<Survey> getSurveysByCreator(Long creatorId);
    Optional<Survey> getSurveyById(Long id);
}
