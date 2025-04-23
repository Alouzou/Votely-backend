package com.alouzou.sondage.controllers;

import com.alouzou.sondage.dto.VoteDTO;
import com.alouzou.sondage.entities.Vote;
import com.alouzou.sondage.services.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
@Slf4j
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PreAuthorize("hasAnyRole('USER', 'CREATOR')")
    @PostMapping
    public ResponseEntity<VoteDTO> vote(@RequestBody VoteDTO dto) {
        Vote savedVote = voteService.vote(dto);
        return ResponseEntity.ok(VoteDTO.fromEntity(savedVote));
    }

}
