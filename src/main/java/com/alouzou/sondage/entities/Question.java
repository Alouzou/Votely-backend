package com.alouzou.sondage.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Choice> choices = new ArrayList<>();

    public Question() {
    }

    public Question(Long id, String text, Survey survey, List<Choice> choices) {
        this.id = id;
        this.questionText = text;
        this.survey = survey;
        this.choices = choices;
    }
    public Question(Long id) {
        this.id = id;
    }

    public Question(String text, Survey survey) {
        this.questionText = text;
        this.survey = survey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String text) {
        this.questionText = text;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}