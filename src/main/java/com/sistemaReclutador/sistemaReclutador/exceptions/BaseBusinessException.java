package com.sistemaReclutador.sistemaReclutador.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BaseBusinessException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    protected BaseBusinessException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}