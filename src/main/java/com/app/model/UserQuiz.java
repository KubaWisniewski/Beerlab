package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserQuiz {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    @OneToMany(mappedBy = "userQuiz", cascade = CascadeType.ALL)
    private List<UserAnswer> userAnswers = new ArrayList<>();
    private Integer score = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserQuiz userQuiz = (UserQuiz) o;
        return Objects.equals(id, userQuiz.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}