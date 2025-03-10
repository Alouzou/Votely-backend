package com.alouzou.sondage.repositories;

import com.alouzou.sondage.entities.Question;
import com.alouzou.sondage.entities.Response;
import com.alouzou.sondage.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findByUser(User user);
    List<Response> findByQuestion(Question question);
    Optional<Response> findByUserAndQuestion(User user, Question question);
}
