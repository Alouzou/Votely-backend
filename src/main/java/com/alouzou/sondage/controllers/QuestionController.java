package com.alouzou.sondage.controllers;

import com.alouzou.sondage.dto.QuestionDTO;
import com.alouzou.sondage.entities.Question;
import com.alouzou.sondage.repositories.QuestionRepository;
import com.alouzou.sondage.services.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(("api/questions"))
public class QuestionController {


    @Autowired
    QuestionService questionService;

    @PostMapping("/create")
    public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionDTO dto){
        Question createdQuestion = questionService.createQuestion(dto);
        return ResponseEntity
                .ok(QuestionDTO.fromEntity(createdQuestion));
    }
}
