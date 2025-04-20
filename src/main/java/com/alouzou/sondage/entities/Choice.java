package com.alouzou.sondage.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "choices")
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String choiceText;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;


    public Choice() {
    }

    public Choice(Long id, String choiceText, Question question) {
        this.id = id;
        this.choiceText = choiceText;
        this.question = question;
    }
    public Choice(Long id) {
        this.id = id;
    }

    public Choice(String choiceText, Question question) {
        this.choiceText = choiceText;
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String choiceText) {
        this.choiceText = choiceText;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}