package com.alouzou.sondage.repositories;

import com.alouzou.sondage.entities.Choice;
import com.alouzou.sondage.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long> {
    List<Choice> findByQuestion(Question question);
}
