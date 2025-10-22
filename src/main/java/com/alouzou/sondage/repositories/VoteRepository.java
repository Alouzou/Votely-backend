package com.alouzou.sondage.repositories;

import com.alouzou.sondage.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END " +
            "FROM Vote v " +
            "WHERE v.user.id = :userId " +
            "AND v.choice.question.id = :questionId")
    boolean existsByUserIdAndQuestionId(@Param("userId") Long userId, @Param("questionId") Long questionId);

}
