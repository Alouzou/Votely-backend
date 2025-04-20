package com.alouzou.sondage.dto;

import com.alouzou.sondage.entities.Category;
import com.alouzou.sondage.entities.Question;
import com.alouzou.sondage.entities.Survey;
import com.alouzou.sondage.entities.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class SurveyDTO {
    private Long id;

    @NotBlank(message = "Veuillez entrer le titre du sondage")
    private String title;

    @Getter
    @NotNull(message = "Veuillez entrer le créateur du sondage")
    private Long creatorId;

    @Getter
    @NotNull(message = "Veuillez entrer la catégorie du sondage")
    private Long categoryId;
    private List<QuestionDTO> questions;


    public SurveyDTO() {
    }

    public SurveyDTO(Long id, String title, Long creatorId, Long categoryId) {
        this.id = id;
        this.title = title;
        this.creatorId = creatorId;
        this.categoryId = categoryId;
    }
    public SurveyDTO(Long id, String title, Long creatorId, Long categoryId, List<QuestionDTO> questions) {
        this.id = id;
        this.title = title;
        this.creatorId = creatorId;
        this.categoryId = categoryId;
        this.questions = questions;
    }

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
                .categoryId(survey.getCategory().getId())
                .questions(
                        survey.getQuestions()
                                .stream()
                                .map(QuestionDTO::fromEntity)
                                .collect(Collectors.toList())
                )
                .build();
    }

}
