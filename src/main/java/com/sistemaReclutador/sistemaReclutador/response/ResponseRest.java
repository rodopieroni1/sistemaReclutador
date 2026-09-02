package com.sistemaReclutador.sistemaReclutador.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseRest<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String errorCode;

    // Métodos Factory Estáticos

    public static <T> ResponseRest<T> success(T data, String message) {
        return ResponseRest.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .errorCode("200")
                .build();
    }

    public static <T> ResponseRest<T> success(T data, String message, String statusCode) {
        return ResponseRest.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .errorCode(statusCode)
                .build();
    }

    public static <T> ResponseRest<T> error(String message, String errorCode) {
        return ResponseRest.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .errorCode(errorCode)
                .build();
    }
}