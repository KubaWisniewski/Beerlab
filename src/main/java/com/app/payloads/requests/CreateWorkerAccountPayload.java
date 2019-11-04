package com.app.payloads.requests;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
@Builder
public class CreateWorkerAccountPayload {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String role;
}
