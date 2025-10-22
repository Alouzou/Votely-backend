package com.alouzou.sondage.services;

import com.alouzou.sondage.dto.VoteDTO;
import com.alouzou.sondage.entities.Vote;

public interface VoteService {
    Vote submitVote(VoteDTO dto);
    boolean hasUserAlreadyVotedForQuestion(Long choiceId, Long userId);
    boolean hasUserVotedForQuestion(Long questionId);
}
