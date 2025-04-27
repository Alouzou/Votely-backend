package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.dto.ChoiceDTO;
import com.alouzou.sondage.entities.Choice;
import com.alouzou.sondage.entities.Question;
import com.alouzou.sondage.entities.RoleName;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.exceptions.ForbiddenActionException;
import com.alouzou.sondage.repositories.ChoiceRepository;
import com.alouzou.sondage.repositories.QuestionRepository;
import com.alouzou.sondage.services.ChoiceService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
public class ChoiceServiceImpl implements ChoiceService {

    @Autowired
    private ChoiceRepository choiceRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AuthService authService;

    @Override
    public Choice addChoice(ChoiceDTO choiceDTO, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question non trouvée"));
        User currentUser = authService.getCurrentUser();
        boolean isAdmin = authService.hasRole(currentUser, RoleName.ROLE_ADMIN);
        if (!isAdmin) {
            if (!currentUser.getId().equals(question.getSurvey().getCreator().getId())) {
                throw new ForbiddenActionException("Seul le créateur de la question peut ajouter un choix.");
            }
        }
        Choice choice = new Choice();
        choice.setChoiceText(choiceDTO.getChoiceText());
        choice.setQuestion(question);
        return choiceRepository.save(choice);
    }
}
