package com.alouzou.sondage.dto;

import com.alouzou.sondage.entities.Choice;
import com.alouzou.sondage.entities.Question;
import com.alouzou.sondage.entities.Survey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

public class QuestionDTO {
    @Getter
    private Long id;
    @Getter
    @NotBlank(message = "Veuillez entrer la question!")
    private String text;

    private Long surveyId;

    public QuestionDTO(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public QuestionDTO(){

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public static Question toEntity(QuestionDTO dto) {
        Question question = new Question();
        question.setText(dto.getText());
        return question;
    }

    public static QuestionDTO fromEntity(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setText(question.getText());
        if(question.getSurvey() != null){
            dto.setSurveyId(question.getSurvey().getId());
        }
        return dto;
    }
}
