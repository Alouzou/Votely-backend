package com.alouzou.sondage.repositories;

import com.alouzou.sondage.entities.Question;
import com.alouzou.sondage.entities.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySurvey(Survey survey);
    Optional<Question> findQuestionById(Long questionId);
    void deleteById(Long questionId);
    List<Question> findBySurveyId(Long surveyId);
}
