package com.alouzou.sondage.repositories;

import com.alouzou.sondage.dto.VoteDTO;
import com.alouzou.sondage.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END " +
            "FROM Vote v " +
            "WHERE v.user.id = :userId " +
            "AND v.choice.question.id = :questionId")
    boolean existsByUserIdAndQuestionId(@Param("userId") Long userId, @Param("questionId") Long questionId);

    @Query("SELECT v FROM Vote v " +
            "JOIN FETCH v.choice c " +
            "JOIN FETCH c.question q " +
            "WHERE v.user.id = :userId AND q.survey.id = :surveyId")
    List<Vote> findByUserIdAndSurveyId(@Param("userId") Long userId,
                                       @Param("surveyId") Long surveyId);

    @Query("SELECT v FROM Vote v " +
            "JOIN FETCH v.choice c " +
            "WHERE v.user.id = :userId AND c.question.id = :questionId")
    Optional<Vote> findByUserIdAndQuestionId(@Param("userId") Long userId,
                                             @Param("questionId") Long questionId);


    @Query("SELECT c.id FROM Vote v " +
            "JOIN v.choice c " +
            "WHERE v.user.id = :userId AND c.question.id = :questionId")
    Optional<Long> findChoiceIdByUserAndQuestion(@Param("userId") Long userId,
                                                 @Param("questionId") Long questionId);

}
