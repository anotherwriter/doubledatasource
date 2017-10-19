package com.example.demo.model.db;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {

    @JsonProperty("error_code")
    private int errorCode = 0;

    @JsonProperty("error_msg")
    private String errorMsg = "ok";

    @JsonProperty("data")
    private T data = null;

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(int code, String message) {
        this.errorCode = code;
        this.errorMsg = message;
    }

    public static final ApiResponse EmptyResponse = new ApiResponse<>();
}
