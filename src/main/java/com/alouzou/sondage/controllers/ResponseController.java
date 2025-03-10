package com.alouzou.sondage.controllers;

import com.alouzou.sondage.entities.Response;
import com.alouzou.sondage.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/responses")
public class ResponseController {
    @Autowired
    private ResponseService responseService;

    @PostMapping("/submit")
    public ResponseEntity<Response> submitResponse(@RequestParam Long userId,
                                                   @RequestParam Long questionId,
                                                   @RequestParam Long choiceId) {
        Response response = responseService.submitResponse(userId, questionId, choiceId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Response>> getResponsesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(responseService.getResponsesByUser(userId));
    }
}

