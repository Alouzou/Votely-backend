package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.dto.UserChoiceDTO;
import com.alouzou.sondage.entities.Choice;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.entities.UserChoice;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.repositories.ChoiceRepository;
import com.alouzou.sondage.repositories.UserChoiceRepository;
import com.alouzou.sondage.repositories.UserRepository;
import com.alouzou.sondage.services.UserChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserChoiceServiceImpl implements UserChoiceService {

    private UserRepository userRepository;
    private ChoiceRepository choiceRepository;
    private UserChoiceRepository userChoiceRepository;

    @Autowired
    public UserChoiceServiceImpl(UserRepository userRepository, ChoiceRepository choiceRepository, UserChoiceRepository userChoiceRepository) {
        this.userRepository = userRepository;
        this.choiceRepository = choiceRepository;
        this.userChoiceRepository = userChoiceRepository;
    }

    @Override
    public UserChoice vote(UserChoiceDTO dto) {
        if (userChoiceRepository.existsByUserIdAndChoiceId(dto.getUserId(), dto.getChoiceId())) {
            throw new IllegalArgumentException("L'utilisateur a déjà voté pour ce choix.");
        }
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
        Choice choice = choiceRepository.findById(dto.getChoiceId())
                .orElseThrow(() -> new EntityNotFoundException("Choix introuvable"));

        UserChoice userChoice = UserChoice.builder()
                .user(user)
                .choice(choice)
                .build();

        return userChoiceRepository.save(userChoice);
    }
}
