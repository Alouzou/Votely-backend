package com.alouzou.sondage.services;

import com.alouzou.sondage.entities.Response;

import java.util.List;

public interface ResponseService {
    Response submitResponse(Long userId, Long questionId, Long choiceId);
    List<Response> getResponsesByUser(Long userId);
}
