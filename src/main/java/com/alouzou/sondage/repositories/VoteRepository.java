package com.alouzou.sondage.repositories;

import com.alouzou.sondage.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByUserIdAndChoiceId(Long userId, Long choiceId);
}
