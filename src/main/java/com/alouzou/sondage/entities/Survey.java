package com.alouzou.sondage.entities;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "surveys")
@EntityListeners(AuditingEntityListener.class)
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @CreatedDate
    @Column(name = "createdAt", nullable = false, updatable = false )
    private Instant createdAt;

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    public Survey() {
    }

    public Survey(String title, User creator, Category category) {
        this.title = title;
        this.creator = creator;
        this.category = category;
    }

    public Survey(Long id, String title, User creator, Category category, List<Question> questions) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.category = category;
        this.questions = questions;
    }

    public Survey(Long id) {
        this.id = id;
    }

    public Survey(String title, Category category, User creator) {
        this.title = title;
        //this.description = description;
        this.category = category;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}