package com.app.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionInfo {
    private ExceptionCode exceptionCode;
    private String exceptionDescription;
    private LocalDateTime exceptionDateTime;
}
