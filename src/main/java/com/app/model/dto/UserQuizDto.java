package com.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQuizDto {
    private Long id;
    private UserDto userDto;
    private QuizDto quizDto;
    private List<UserAnswerDto> userAnswerDtos = new ArrayList<>();
    private Integer score;
}
