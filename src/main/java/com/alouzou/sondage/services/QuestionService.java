package com.alouzou.sondage.services;

import com.alouzou.sondage.dto.QuestionDTO;
import com.alouzou.sondage.entities.Question;

public interface QuestionService {
    Question createQuestion(QuestionDTO questionDto);
}
