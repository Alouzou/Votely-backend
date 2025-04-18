package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.entities.Category;
import com.alouzou.sondage.exceptions.CategoryInactiveException;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.exceptions.ResourceAlreadyUsedException;
import com.alouzou.sondage.repositories.CategoryRepository;
import com.alouzou.sondage.repositories.SurveyRepository;
import com.alouzou.sondage.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Override
    public Category createCategory(String nameCategory, Boolean isActive) {
        if(nameCategory.trim().isEmpty()){
            throw new IllegalArgumentException("Le nom de la catégorie ne doit pas être vide!");
        }
        if(categoryRepository.findByName(nameCategory.trim()).isPresent()){
            throw new ResourceAlreadyUsedException("Catégorie déja existante!");
        }
        if(isActive == null){
            isActive = true;
        }
        return categoryRepository.save(new Category(nameCategory, isActive));
    }

    @Override
    public Category modifyCategory(Long id, Category category) {
        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La catégorie avec l'id " + id + " non trouvée!"));
        if (!cat.isActive()) {
            throw new CategoryInactiveException("La catégorie avec l'id " + id + " est désactivée et ne peut pas être modifiée.");
        }
        if(category.getName() != null){
            if(category.getName().trim().isEmpty()){
                throw new IllegalArgumentException("Le nom de la catégorie ne doit pas être vide!");
            }
            if(categoryRepository.findByName(category.getName()).isPresent()){
                throw new ResourceAlreadyUsedException("Le nom choisi est dèja utilisé!");
            }
            cat.setName(category.getName());
        }

        if(category.isActive() != null){
            cat.setActive(category.isActive());
        }

        return categoryRepository.save(cat);

    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void deleteById(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie non trouvée avec l'ID : " + id));
        if(!surveyRepository.findByCategory_Id(id).isEmpty()){
            category.setActive(false);
            categoryRepository.save(category);
            throw new ResourceAlreadyUsedException("La catégorie avec l'id " + id + " est dèja utilisée dans d'autres sondages.\nLa catégorie a été désactivée");
        }
        categoryRepository.deleteById(id);
    }

}
