package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.dto.VoteDTO;
import com.alouzou.sondage.entities.Choice;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.entities.Vote;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.repositories.ChoiceRepository;
import com.alouzou.sondage.repositories.VoteRepository;
import com.alouzou.sondage.repositories.UserRepository;
import com.alouzou.sondage.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService {

    private UserRepository userRepository;
    private ChoiceRepository choiceRepository;
    private VoteRepository voteRepository;

    @Autowired
    public VoteServiceImpl(UserRepository userRepository, ChoiceRepository choiceRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.choiceRepository = choiceRepository;
        this.voteRepository = voteRepository;
    }

    @Override
    public Vote vote(VoteDTO dto) {
        if (voteRepository.existsByUserIdAndChoiceId(dto.getUserId(), dto.getChoiceId())) {
            throw new IllegalArgumentException("L'utilisateur a déjà voté pour ce choix.");
        }
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
        Choice choice = choiceRepository.findById(dto.getChoiceId())
                .orElseThrow(() -> new EntityNotFoundException("Choix introuvable"));

        Vote vote = Vote.builder()
                .user(user)
                .choice(choice)
                .build();

        return voteRepository.save(vote);
    }
}
