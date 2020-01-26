package com.app.repository;

import com.app.model.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    Optional<UserAnswer> findByQuestionIdAndUserQuizId(Long questionId,Long userQuizId);
}
