package com.alouzou.sondage.controllers;

import com.alouzou.sondage.dto.VoteDTO;
import com.alouzou.sondage.entities.Vote;
import com.alouzou.sondage.services.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PreAuthorize("hasAnyRole('USER', 'CREATOR')")
    @PostMapping("/submit")
    public ResponseEntity<VoteDTO> vote(@RequestBody VoteDTO dto) {
        voteService.submitVote(dto);
        return ResponseEntity.ok(dto);
    }

}
