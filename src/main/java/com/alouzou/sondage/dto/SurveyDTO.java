package com.alouzou.sondage.dto;

import com.alouzou.sondage.entities.Category;
import com.alouzou.sondage.entities.Survey;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SurveyDTO {
    private Long id;
    private String title;
    private String description;
    private Long creatorId;
    private Long categoryId;

    public static SurveyDTO fromEntity(Survey survey) {
        if (survey == null) {
            return null;
        }

        return SurveyDTO.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .creatorId(survey.getCreator().getId())
                .categoryId(survey.getCategory().getId())
                .build();

    }

    public static Survey toEntity(SurveyDTO dto) {
        if (dto == null) {
            return null;
        }

        Survey survey = new Survey();
        survey.setId(dto.id);
        survey.setTitle(dto.getTitle());
        survey.setDescription(dto.getDescription());

        return survey;

    }


}
