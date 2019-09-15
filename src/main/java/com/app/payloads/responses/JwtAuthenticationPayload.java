package com.app.payloads.responses;

import com.app.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtAuthenticationPayload {
    private String accessToken;
    private String tokenType;
    private User user;
}
