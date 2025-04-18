package com.alouzou.sondage.repositories;

import com.alouzou.sondage.entities.Category;
import com.alouzou.sondage.entities.Survey;
import com.alouzou.sondage.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Logger logger = LoggerFactory.getLogger(SurveyRepository.class);

    List<Survey> findByCategory_Id(Long categoryId);
    List<Survey> findByCategory(Category category);
    List<Survey> findByCreator(User creator);

    default List<Survey> findSurveysByCategoryWithLog(Category category) {
        logger.info("Recherche des sondages dans la catégorie : {}", category.getName());
        return findByCategory(category);
    }
}
