package com.alouzou.sondage.controllers;

import com.alouzou.sondage.entities.Category;
import com.alouzou.sondage.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/categories")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;


    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        Category cat = categoryService.createCategory(category.getName(), category.isActive());
        return ResponseEntity.ok(cat);
    }

    @PatchMapping("/modify/{CategoryId}")
    public ResponseEntity<Category> modifyCategory(
            @PathVariable("CategoryId") Long id,
            @RequestBody Category category){
        Category cat = categoryService.modifyCategory(id, category);
        return ResponseEntity.ok(cat);
    }


    @GetMapping("/all")
    public ResponseEntity<List<Category>> findAll(){
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id){

        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
