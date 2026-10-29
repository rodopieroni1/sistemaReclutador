package com.sistemaReclutador.sistemaReclutador.strategies.impl;

import org.springframework.stereotype.Component;
import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;
import com.sistemaReclutador.sistemaReclutador.strategies.EmpresaValidationStrategy;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RubroExistenteStrategyImpl implements EmpresaValidationStrategy {

	private final EmpresaRepository empresaRepository;

	@Override
	public void validar(EmpresaRequest request, Empresa empresaExistente) {

		// Al crear una empresa, el rubro es obligatorio.
		if (empresaExistente == null && request.getIdRubro() == null) {
			throw new IllegalArgumentException("Debe seleccionar un rubro para la empresa.");
		}

		// Si no se envía un rubro al modificar, no se valida aquí.
		if (request.getIdRubro() == null) {
			return;
		}

		// Si no existe una empresa previa o esta no tiene rubro,
		// no hay un rubro anterior que comparar.
		if (empresaExistente == null || empresaExistente.getRubro() == null) {
			return;
		}

		boolean mismoRubro = empresaExistente.getRubro().getIdRubro().equals(request.getIdRubro());
		if (!mismoRubro) {
			return;
		}
	}

}
