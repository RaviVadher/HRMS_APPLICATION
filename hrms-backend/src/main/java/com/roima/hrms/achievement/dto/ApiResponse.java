package com.roima.hrms.achievement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private Boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private Integer statusCode;

    // Constructor for success response
    public ApiResponse(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Constructor for error response
    public ApiResponse(Boolean success, String message, List<String> errors, Integer statusCode) {
        this.success = success;
        this.message = message;
        this.errors = errors;
        this.statusCode = statusCode;
    }
}

