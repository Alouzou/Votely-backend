package com.alouzou.sondage.services;

import com.alouzou.sondage.entities.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findAll();

    Optional<Category> findById(Long id);
}
