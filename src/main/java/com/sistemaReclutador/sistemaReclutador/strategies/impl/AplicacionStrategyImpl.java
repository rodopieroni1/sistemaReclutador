package com.sistemaReclutador.sistemaReclutador.strategies.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.sistemaReclutador.sistemaReclutador.dto.AplicacionesMiasResponse;
import com.sistemaReclutador.sistemaReclutador.strategies.AplicacionesMiasStrategy;

@Component("standardMappingStrategy")
public class AplicacionStrategyImpl implements AplicacionesMiasStrategy {

	@Override
	public AplicacionesMiasResponse map(Object[] row) {
	    return AplicacionesMiasResponse.builder()
	            .idAplicacion((Integer) row[0])
	            .puesto((String) row[1])
	            .empresa((String) row[2])
	            .fecha(row[3] instanceof java.sql.Timestamp ? 
	                    ((java.sql.Timestamp) row[3]).toLocalDateTime() : (LocalDateTime) row[3])
	            .estado((Boolean) row[4])
	            .descripcionOferta((String) row[5])
	            .fotoOferta((String) row[6])
	            .email((String) row[7])
	            .telefono((String) row[8])
	            .direccion((String) row[9])
	            .idOferta(row[10] != null ? ((Number) row[10]).longValue() : null)
	            .build();
	}
}
