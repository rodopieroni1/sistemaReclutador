package com.sistemaReclutador.sistemaReclutador.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
	    String type,
	    String title,
	    int status,
	    String detail,
	    LocalDateTime timestamp,
	    Map<String, String> invalidFields // Opcional: solo para errores de validación
	) {
	    public ErrorResponse(String type, String title, int status, String detail) {
	        this(type, title, status, detail, LocalDateTime.now(), null);
	    }
	}