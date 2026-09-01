package com.sistemaReclutador.sistemaReclutador.validators;

import java.util.Optional;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sistemaReclutador.sistemaReclutador.Enum.ResultadosAplicacion;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionResponseDTO;
import com.sistemaReclutador.sistemaReclutador.repositories.PerfilRepository;

import lombok.RequiredArgsConstructor;

@Component
@Order(1)
@RequiredArgsConstructor
public class ValidarPerfilHandler implements ValidacionAplicacionHandler {

	private final PerfilRepository perfilRepository;
	
	@Override
    public Optional<AplicacionResponseDTO> validar(AplicacionRequest request) {
        if (request.getIdPerfil() == null || request.getIdPerfil().getId_perfil() == null) {
            return Optional.of(new AplicacionResponseDTO(ResultadosAplicacion.PERFIL_INVALIDO, "Debe indicar un perfil válido."));
        }
        if (!perfilRepository.existsById(request.getIdPerfil().getId_perfil())) {
            return Optional.of(new AplicacionResponseDTO(ResultadosAplicacion.PERFIL_NO_ENCONTRADO, "El perfil indicado no existe."));
        }
        return Optional.empty();
    }
}
