package com.alouzou.sondage.dto;

import com.alouzou.sondage.entities.Category;
import com.alouzou.sondage.entities.Question;
import com.alouzou.sondage.entities.Survey;
import com.alouzou.sondage.entities.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SurveyDTO {
    private Long id;

    @NotBlank(message = "Veuillez entrer le titre du sondage")
    private String title;

    @Getter
    @NotNull(message = "Veuillez entrer le créateur du sondage")
    private Long creatorId;

    @Getter
    private String creatorName;

    @Getter
    private Instant createdAt;

    @Getter
    private String categoryName;

    @Getter
    @NotNull(message = "Veuillez entrer la catégorie du sondage")
    private Long categoryId;
    private List<QuestionDTO> questions;


    public Survey toEntity(User creator, Category category) {
        Survey survey = new Survey();
        survey.setId(this.id);
        survey.setTitle(this.title);
        survey.setCreator(creator);
        survey.setCategory(category);
        return survey;
    }

    public static SurveyDTO fromEntity(Survey survey) {

        return SurveyDTO.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .creatorId(survey.getCreator().getId())
                .creatorName(survey.getCreator().getUsername())
                .categoryId(survey.getCategory().getId())
                .categoryName(survey.getCategory().getName())
                .questions(
                        survey.getQuestions()
                                .stream()
                                .map(QuestionDTO::fromEntityWithVotes)
                                .collect(Collectors.toList())
                )
                .createdAt(survey.getCreatedAt())
                .build();
    }

}
