package com.alouzou.sondage.repositories;

import com.alouzou.sondage.entities.UserChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChoiceRepository extends JpaRepository<UserChoice, Long> {
    boolean existsByUserIdAndChoiceId(Long userId, Long choiceId);
}
