package com.sistemaReclutador.sistemaReclutador.validators;

import java.util.Optional;

import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionResponseDTO;

public interface  ValidacionAplicacionHandler {

	Optional<AplicacionResponseDTO> validar(AplicacionRequest request);
}
