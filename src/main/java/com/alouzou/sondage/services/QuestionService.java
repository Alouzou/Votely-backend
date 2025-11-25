package com.alouzou.sondage.services;

import com.alouzou.sondage.dto.QuestionDTO;
import com.alouzou.sondage.entities.Question;
import com.alouzou.sondage.entities.Survey;

public interface QuestionService {
    Question createQuestion(QuestionDTO questionDto);
    void deleteQuestionById(Long questionId, Long surveyId);
}
