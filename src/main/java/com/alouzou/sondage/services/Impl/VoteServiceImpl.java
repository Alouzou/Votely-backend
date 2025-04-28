package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.dto.VoteDTO;
import com.alouzou.sondage.entities.Choice;
import com.alouzou.sondage.entities.Survey;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.entities.Vote;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.exceptions.ForbiddenActionException;
import com.alouzou.sondage.repositories.ChoiceRepository;
import com.alouzou.sondage.repositories.VoteRepository;
import com.alouzou.sondage.repositories.UserRepository;
import com.alouzou.sondage.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService {

    private AuthService authService;
    private UserRepository userRepository;
    private ChoiceRepository choiceRepository;
    private VoteRepository voteRepository;

    @Autowired
    public VoteServiceImpl(AuthService authService, UserRepository userRepository, ChoiceRepository choiceRepository, VoteRepository voteRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.choiceRepository = choiceRepository;
        this.voteRepository = voteRepository;
    }

    public boolean hasUserAlreadyVotedForSurvey(Long choiceId, Long userId){
        Choice choice = choiceRepository.findById(choiceId)
                .orElseThrow(() -> new EntityNotFoundException("Choix non trouvé"));

        Long surveyId = choice.getQuestion().getSurvey().getId();

        return voteRepository.existsByUserIdAndSurveyId(userId, surveyId);

    }

    @Override
    public Vote vote(VoteDTO dto) {
        User userPrincipal = authService.getCurrentUser();
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
        Choice choice = choiceRepository.findById(dto.getChoiceId())
                .orElseThrow(() -> new EntityNotFoundException("Choix introuvable"));

        if (voteRepository.existsByUserIdAndChoiceId(userPrincipal.getId(), dto.getChoiceId())) {
            throw new IllegalArgumentException("L'utilisateur a déjà voté pour ce choix.");
        }
        if(hasUserAlreadyVotedForSurvey(dto.getChoiceId(), user.getId())){
            throw new ForbiddenActionException("Vous avez déjà voté pour ce sondage.");
        }

        Vote vote = Vote.builder()
                .user(user)
                .choice(choice)
                .build();

        return voteRepository.save(vote);
    }
}
