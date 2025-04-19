package com.alouzou.sondage.services;

import com.alouzou.sondage.entities.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryService {


    List<Category> findAll();

    Category findById(Long id);
    Category createCategory(String name, Boolean isActive);

    Category modifyCategory(Long id, Category category);
    void deleteById(Long id);
    List<Category> findAllByIsActive();
}
