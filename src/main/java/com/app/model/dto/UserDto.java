package com.app.model.dto;

import com.app.model.Gender;
import com.app.model.UserQuiz;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Gender gender;
    private String email;
    private List<RoleDto> rolesDto = new LinkedList<>();
    private Double balance;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private List<UserQuizDto> userQuizDtos = new ArrayList<>();
}
