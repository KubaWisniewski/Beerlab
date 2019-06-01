package com.app.payloads.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtAuthenticationPayload {
    private String accessToken;
    private String tokenType;
}