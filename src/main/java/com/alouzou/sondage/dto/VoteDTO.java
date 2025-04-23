package com.alouzou.sondage.dto;

import com.alouzou.sondage.entities.UserChoice;
import lombok.Data;

@Data
public class UserChoiceDTO {
    private Long userId;
    private Long choiceId;


    public UserChoiceDTO(Long userId, Long choiceId) {
        this.userId = userId;
        this.choiceId = choiceId;
    }

    public static UserChoiceDTO fromEntity(UserChoice userChoice) {
        return new UserChoiceDTO(
                userChoice.getUser().getId(),
                userChoice.getChoice().getId()
        );
    }

}
