package com.example.demo.server.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestExceptionResponse {
    @NotNull
    private int code;

    @NotNull
    private String message;

    @NotNull
    private String requestId;
}
