package com.baraigad.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public final class ResponseUtil {

    private ResponseUtil() {}

    public static <T> ResponseEntity<ApiResponse<T>> success(
            String message,
            T data) {


        return ResponseEntity.ok(
                ApiResponse.<T>builder()
                        .success(true)
                        .message(message)
                        .errors(null)
                        .data(data)
                        .build()
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(
            String message,
            T data) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<T>builder()
                                .success(true)
                                .message(message)
                                .errors(null)
                                .data(data)
                                .build()
                );
    }

    public static ResponseEntity<ApiResponse<Object>> error(
            String message,
            Object errors,
            HttpStatus status) {

        return ResponseEntity.status(status)
                .body(
                        ApiResponse.builder()
                                .success(false)
                                .message(message)
                                .errors(errors)
                                .build()
                );
    }
}