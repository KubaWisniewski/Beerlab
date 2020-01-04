package com.app.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserScore {
    private String username;
    private Integer numberOfDoneQuizes=0;
    private Integer totalScore=0;
}
