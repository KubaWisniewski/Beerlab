package com.app.payloads.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAnswerPayload {
    private String text;
    private String imgUrl;
    @JsonProperty("isCorrect")
    private boolean isCorrect;
}
