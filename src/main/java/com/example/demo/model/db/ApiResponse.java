package com.example.demo.model.db;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {

    private int code = 0;

    private String message = "ok";

    private T data = null;

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static final ApiResponse EmptyResponse = new ApiResponse<>();
}
