package com.alouzou.sondage.dto;

import com.alouzou.sondage.entities.Choice;
import com.alouzou.sondage.entities.Question;
import com.alouzou.sondage.entities.Survey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {

    private Long id;
    @NotBlank(message = "Veuillez entrer la question!")
    private String questionText;
    private Long surveyId;
    private List<ChoiceDTO> choices;

    public static Question toEntity(QuestionDTO dto) {
        Question question = new Question();
        question.setQuestionText(dto.getQuestionText());
        return question;
    }


    public static QuestionDTO fromEntity(Question question) {
        return QuestionDTO.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .surveyId(question.getSurvey().getId())
                .choices(
                        question.getChoices()
                                .stream()
                                .map(ChoiceDTO::fromEntity)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public static QuestionDTO fromEntityWithVotes(Question question) {
        return QuestionDTO.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .surveyId(question.getSurvey().getId())
                .choices(
                        question.getChoices()
                                .stream()
                                .map(ChoiceDTO::fromEntityWithVotes)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
