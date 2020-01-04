package com.app.repository;

import com.app.model.UserQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuizRepository extends JpaRepository<UserQuiz, Long> {
    Optional<UserQuiz> findByUserIdAndQuizId(Long userId, Long quizId);
    List<UserQuiz> findByUserId(Long userId);

}
