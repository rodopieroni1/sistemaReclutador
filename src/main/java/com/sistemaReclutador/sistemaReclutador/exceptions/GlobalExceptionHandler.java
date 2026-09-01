package com.sistemaReclutador.sistemaReclutador.exceptions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sistemaReclutador.sistemaReclutador.response.ResponseRest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BaseBusinessException.class)
    public ResponseEntity<ResponseRest<Object>> handleBaseBusinessException(BaseBusinessException ex) {
        ResponseRest<Object> response = new ResponseRest<>(
            false, 
            ex.getMessage(), 
            null, 
            LocalDateTime.now(), 
            ex.getErrorCode()
        );
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseRest<Object>> handleBadRequest(IllegalArgumentException ex) {
        ResponseRest<Object> response = new ResponseRest<>(false, ex.getMessage(), null, LocalDateTime.now(), "400");
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseRest<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errores = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ResponseRest<Object> response = new ResponseRest<>(false, errores, null, LocalDateTime.now(), "400");
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseRest<Object>> handleGeneralException(Exception ex) {
        ResponseRest<Object> response = new ResponseRest<>(false, "Ocurrió un error interno en el servidor", null, LocalDateTime.now(), "500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}