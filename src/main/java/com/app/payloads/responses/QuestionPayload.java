package com.app.payloads.responses;

import com.app.model.dto.AnswerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionPayload {
    private Long id;
    private String text;
    private String imgUrl;
    private List<AnswerDto> answers = new ArrayList<>();
}
