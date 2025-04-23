package com.alouzou.sondage.dto;

import com.alouzou.sondage.entities.Vote;
import lombok.Data;

@Data
public class VoteDTO {
    private Long userId;
    private Long choiceId;


    public VoteDTO(Long userId, Long choiceId) {
        this.userId = userId;
        this.choiceId = choiceId;
    }

    public static VoteDTO fromEntity(Vote vote) {
        return new VoteDTO(
                vote.getUser().getId(),
                vote.getChoice().getId()
        );
    }

}
