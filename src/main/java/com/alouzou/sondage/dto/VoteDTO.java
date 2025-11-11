package com.alouzou.sondage.dto;

import com.alouzou.sondage.entities.Vote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO {

    private Long id;
    private Long choiceId;
    private Long userId;
    private Instant createdAt;

    public VoteDTO(Long choiceId) {
        this.choiceId = choiceId;
    }

    public static VoteDTO fromEntity(Vote vote) {
        return new VoteDTO(
                vote.getId(),
                vote.getChoice().getId(),
                vote.getUser().getId(),
                vote.getCreatedAt()
        );
    }
}
