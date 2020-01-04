package com.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAnswerDto {
    private Long id;
    private QuestionDto questionDto;
    private AnswerDto answerDto;
    private UserQuizDto userQuizDto;
}
