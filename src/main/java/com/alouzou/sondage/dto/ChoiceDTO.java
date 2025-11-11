package com.alouzou.sondage.dto;

import com.alouzou.sondage.entities.Choice;
import com.alouzou.sondage.entities.Question;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ChoiceDTO {

    private Long choiceId;

    @NotBlank(message = "Veuillez entre le choix")
    private String choiceText;

    private List<VoteDTO> votes;

    @NotNull(message = "Veuillez choisir la question")
    @JsonIgnore
    private Long questionId;

    public ChoiceDTO(String choiceText) {
        this.choiceText = choiceText;
    }

    public ChoiceDTO(String choiceText, Long questionId) {
        this.choiceText = choiceText;
        this.questionId = questionId;
    }
    public ChoiceDTO(Long choiceId, String choiceText, Long questionId, List<VoteDTO> votes) {
        this.choiceId = choiceId;
        this.choiceText = choiceText;
        this.questionId = questionId;
        this.votes = votes;
    }

    public ChoiceDTO(String choiceText, Long questionId, Long choiceId) {
        this.choiceId = choiceId;
        this.choiceText = choiceText;
        this.questionId = questionId;
    }


    public static ChoiceDTO fromEntity(Choice choice) {
        return new ChoiceDTO(choice.getChoiceText(), choice.getQuestion().getId(), choice.getId());
    }

    public static ChoiceDTO fromEntityWithVotes(Choice choice) {
        List<VoteDTO> voteDTOs = choice.getVotes()
                .stream()
                .map(VoteDTO::fromEntity)
                .toList();


        return new ChoiceDTO(
                choice.getId(),
                choice.getChoiceText(),
                choice.getQuestion().getId(),
                voteDTOs
        );
    }

    public static Choice toEntity(ChoiceDTO choiceDTO, Question question) {
        return new Choice(choiceDTO.getChoiceText(), question);
    }
}
