package com.alouzou.sondage.dto;

import com.alouzou.sondage.entities.Vote;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteDTO {

    private Long choiceId;

    public VoteDTO(Long choiceId) {
        this.choiceId = choiceId;
    }

    public static VoteDTO fromEntity(Vote vote) {
        return new VoteDTO(
                vote.getChoice().getId()
        );
    }
}
