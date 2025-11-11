package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.dto.VoteDTO;
import com.alouzou.sondage.entities.Choice;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.entities.Vote;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.exceptions.ForbiddenActionException;
import com.alouzou.sondage.repositories.ChoiceRepository;
import com.alouzou.sondage.repositories.QuestionRepository;
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
    private QuestionRepository questionRepository;

    @Autowired
    public VoteServiceImpl(AuthService authService, UserRepository userRepository, QuestionRepository questionRepository, ChoiceRepository choiceRepository, VoteRepository voteRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.choiceRepository = choiceRepository;
        this.voteRepository = voteRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Vote submitVote(VoteDTO dto) {
        User userPrincipal = authService.getCurrentUser();
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
        Choice choice = choiceRepository.findById(dto.getChoiceId())
                .orElseThrow(() -> new EntityNotFoundException("Choix introuvable"));
        
        if (hasUserAlreadyVotedForQuestion(dto.getChoiceId(), user.getId())) {
            throw new ForbiddenActionException("Vous avez déjà voté pour cette question.");
        }

        Vote vote = Vote.builder()
                .user(user)
                .choice(choice)
                .build();

        return voteRepository.save(vote);
    }

    @Override
    public boolean hasUserAlreadyVotedForQuestion(Long choiceId, Long userId) {
        Choice choice = choiceRepository.findById(choiceId)
                .orElseThrow(() -> new EntityNotFoundException("Choix non trouvé"));

        Long questionId = choice.getQuestion().getId();

        return voteRepository.existsByUserIdAndQuestionId(userId, questionId);
    }

    @Override
    public boolean hasUserVotedForQuestion(Long questionId) {
        User userPrincipal = authService.getCurrentUser();
        return voteRepository.existsByUserIdAndQuestionId(userPrincipal.getId(), questionId);
    }
}
