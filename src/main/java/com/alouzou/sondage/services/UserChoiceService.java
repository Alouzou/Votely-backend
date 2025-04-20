package com.alouzou.sondage.services;

import com.alouzou.sondage.dto.UserChoiceDTO;
import com.alouzou.sondage.entities.UserChoice;

public interface UserChoiceService {
    UserChoice vote(UserChoiceDTO dto);
}
