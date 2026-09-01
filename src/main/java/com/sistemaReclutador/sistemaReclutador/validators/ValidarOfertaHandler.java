package com.sistemaReclutador.sistemaReclutador.validators;

import java.util.Optional;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sistemaReclutador.sistemaReclutador.Enum.ResultadosAplicacion;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionRequest;
import com.sistemaReclutador.sistemaReclutador.dto.AplicacionResponseDTO;
import com.sistemaReclutador.sistemaReclutador.repositories.OfertaRepository;

import lombok.RequiredArgsConstructor;


@Component
@Order(2)
@RequiredArgsConstructor
public class ValidarOfertaHandler implements ValidacionAplicacionHandler  {
	
	private final OfertaRepository ofertaRepository;

	@Override
    public Optional<AplicacionResponseDTO> validar(AplicacionRequest request) {
        if (request.getIdOferta() == null || request.getIdOferta().getIdOferta() == null) {
            return Optional.of(new AplicacionResponseDTO(ResultadosAplicacion.OFERTA_INVALIDA, "Debe indicar una oferta válida."));
        }
        if (!ofertaRepository.existsById(request.getIdOferta().getIdOferta())) {
            return Optional.of(new AplicacionResponseDTO(ResultadosAplicacion.OFERTA_NO_ENCONTRADA, "La oferta indicada no existe."));
        }
        return Optional.empty();
    }
}
