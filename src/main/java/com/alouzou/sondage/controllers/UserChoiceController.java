package com.alouzou.sondage.controllers;

import com.alouzou.sondage.dto.UserChoiceDTO;
import com.alouzou.sondage.entities.UserChoice;
import com.alouzou.sondage.services.UserChoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
@Slf4j
public class UserChoiceController {

    @Autowired
    private UserChoiceService userChoiceService;

    @PostMapping
    public ResponseEntity<UserChoiceDTO> vote(@RequestBody UserChoiceDTO dto) {
        UserChoice savedVote = userChoiceService.vote(dto);
        return ResponseEntity.ok(UserChoiceDTO.fromEntity(savedVote));
    }

}
