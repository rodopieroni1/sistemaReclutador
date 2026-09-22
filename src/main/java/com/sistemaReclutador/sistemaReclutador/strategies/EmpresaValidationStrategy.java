package com.sistemaReclutador.sistemaReclutador.strategies;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;

public interface EmpresaValidationStrategy {
	void validar(EmpresaRequest request, Empresa empresaExistente);
}
