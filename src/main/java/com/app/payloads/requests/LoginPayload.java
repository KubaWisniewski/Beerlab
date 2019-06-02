package com.app.payloads.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginPayload {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
