package com.alouzou.sondage.controllers;

import com.alouzou.sondage.dto.SurveyDTO;
import com.alouzou.sondage.entities.Survey;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @PostMapping("/create")
    public ResponseEntity<SurveyDTO> createSurvey(@RequestBody SurveyDTO survey) {
        Survey createdSurvey = surveyService.createSurvey(
                survey.getTitle(),
                survey.getDescription(),
                survey.getCategoryId(),
                new User(survey.getCreatorId())
        );
        return ResponseEntity.ok(SurveyDTO.fromEntity(createdSurvey));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDTO> getSurveyById(@PathVariable Long id) {
        return surveyService.getSurveyById(id)
                .map(SurveyDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<SurveyDTO>> getSurveysByCategory(@PathVariable Long categoryId) {
        List<SurveyDTO> surveys = surveyService.getSurveysByCategory(categoryId)
                .stream()
                .map(SurveyDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(surveys);

    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<SurveyDTO>> getSurveysByCreator(@PathVariable Long creatorId) {
        List<SurveyDTO> surveys = surveyService.getSurveysByCreator(creatorId)
                .stream()
                .map(SurveyDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(surveys);
    }
}
