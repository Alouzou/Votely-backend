package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.config.DataLoader;
import com.alouzou.sondage.entities.Choice;
import com.alouzou.sondage.entities.Question;
import com.alouzou.sondage.entities.Response;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.repositories.ChoiceRepository;
import com.alouzou.sondage.repositories.QuestionRepository;
import com.alouzou.sondage.repositories.ResponseRepository;
import com.alouzou.sondage.services.ResponseService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponseServiceImpl implements ResponseService {
    private static final Logger log = LoggerFactory.getLogger(ResponseServiceImpl.class);

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    @Override
    public Response submitResponse(Long userId, Long questionId, Long choiceId) {
        log.info("L'utilisateur {} répond à la question {}", userId, questionId);

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question introuvable !"));

        Choice choice = choiceRepository.findById(choiceId)
                .orElseThrow(() -> new RuntimeException("Choix introuvable !"));

        Optional<Response> existingResponse = responseRepository.findByUserAndQuestion(new User(userId), question);
        if (existingResponse.isPresent()) {
            throw new RuntimeException("L'utilisateur a déjà répondu à cette question !");
        }

        Response response = new Response();
        response.setUser(new User(userId));
        response.setQuestion(question);
        response.setChoice(choice);

        return responseRepository.save(response);

    }

    @Override
    public List<Response> getResponsesByUser(Long userId) {

        return responseRepository.findByUser(new User(userId));
    }
}
