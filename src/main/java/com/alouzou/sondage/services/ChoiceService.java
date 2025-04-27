package com.alouzou.sondage.services;

import com.alouzou.sondage.dto.ChoiceDTO;
import com.alouzou.sondage.entities.Choice;

public interface ChoiceService {
    Choice addChoice(ChoiceDTO choiceDTO, Long idQuestion);
}
