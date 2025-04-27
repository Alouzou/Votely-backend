package com.alouzou.sondage.controllers;

import com.alouzou.sondage.dto.ChoiceDTO;
import com.alouzou.sondage.dto.QuestionDTO;
import com.alouzou.sondage.entities.Choice;
import com.alouzou.sondage.entities.Question;
import com.alouzou.sondage.repositories.QuestionRepository;
import com.alouzou.sondage.services.ChoiceService;
import com.alouzou.sondage.services.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(("api/questions"))
public class QuestionController {


    @Autowired
    ChoiceService choiceService;

    @Autowired
    QuestionService questionService;

    @PreAuthorize("hasAnyRole('CREATOR', 'ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionDTO dto){
        Question createdQuestion = questionService.createQuestion(dto);
        return ResponseEntity
                .ok(QuestionDTO.fromEntity(createdQuestion));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CREATOR')")
    @PostMapping("/{idQuestion}/addChoice")
    public ResponseEntity<ChoiceDTO> addChoice(
            @PathVariable("idQuestion") Long idQuestion,
            @RequestBody ChoiceDTO choiceDTO
    ){
        Choice createdChoice = choiceService.addChoice(choiceDTO, idQuestion);
        return ResponseEntity
                .ok(ChoiceDTO.fromEntity(createdChoice));
    }

}
