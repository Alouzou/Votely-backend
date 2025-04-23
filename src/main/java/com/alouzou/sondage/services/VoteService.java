package com.alouzou.sondage.services;

import com.alouzou.sondage.dto.VoteDTO;
import com.alouzou.sondage.entities.Vote;

public interface VoteService {
    Vote vote(VoteDTO dto);
}
