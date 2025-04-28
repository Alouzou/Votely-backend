package com.alouzou.sondage.repositories;

import com.alouzou.sondage.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByUserIdAndChoiceId(Long userId, Long choiceId);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END " +
            "FROM Vote v " +
            "WHERE v.user.id = :userId " +
            "AND v.choice.question.survey.id = :surveyId")
    boolean existsByUserIdAndSurveyId(@Param("userId") Long userId, @Param("surveyId") Long surveyId);
}
