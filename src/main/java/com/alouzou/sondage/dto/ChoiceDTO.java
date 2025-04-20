package com.alouzou.sondage.dto;

import com.alouzou.sondage.entities.Choice;
import com.alouzou.sondage.entities.Question;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChoiceDTO {

    @NotBlank(message = "Veuillez entre le choix")
    private String choiceText;

    @NotNull(message = "Veuillez choisir la question")
    @JsonIgnore
    private Long questionId;

    public ChoiceDTO(String choiceText){
        this.choiceText = choiceText;
    }

    public ChoiceDTO(String choiceText, Long questionId) {
        this.choiceText = choiceText;
        this.questionId = questionId;
    }

        public static ChoiceDTO fromEntity(Choice choice){
        return new ChoiceDTO(choice.getChoiceText(), choice.getQuestion().getId());
    }

    public static Choice toEntity(ChoiceDTO choiceDTO, Question question) {
        return new Choice(choiceDTO.getChoiceText(), question);
    }
}
