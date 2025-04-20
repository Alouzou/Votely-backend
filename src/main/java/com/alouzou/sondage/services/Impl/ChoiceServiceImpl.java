package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.dto.ChoiceDTO;
import com.alouzou.sondage.entities.Choice;
import com.alouzou.sondage.entities.Question;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.repositories.ChoiceRepository;
import com.alouzou.sondage.repositories.QuestionRepository;
import com.alouzou.sondage.services.ChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChoiceServiceImpl implements ChoiceService {

    @Autowired
    private ChoiceRepository choiceRepository;
    @Autowired
    private QuestionRepository questionRepository;


    @Override
    public Choice createChoice(ChoiceDTO choiceDTO) {
        Question question = questionRepository.findById(choiceDTO.getQuestionId())
                .orElseThrow(() -> new EntityNotFoundException("Question non trouvée"));

        Choice choice = new Choice();
        choice.setChoiceText(choiceDTO.getChoiceText());
        choice.setQuestion(question);

        return choiceRepository.save(choice);
    }
}
