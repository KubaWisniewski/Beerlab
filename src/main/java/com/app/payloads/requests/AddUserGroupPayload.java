package com.app.payloads.requests;

import com.app.model.Group;
import com.app.model.dto.GroupDto;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class AddUserGroupPayload {
    @NotBlank
    private String email;

    @NotBlank
    private String groupName;
}
