package com.sistemaReclutador.sistemaReclutador.strategies.impl;

import org.springframework.stereotype.Component;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.strategies.EmpresaValidationStrategy;

@Component
public class LogoNombreValidationStrategy implements EmpresaValidationStrategy {

    @Override
    public void validar(EmpresaRequest request, Empresa empresaExistente) {
        if (request.getLogo() != null && !request.getLogo().isEmpty()) {
            String nombreOriginal = request.getLogo().getOriginalFilename();
            if (nombreOriginal != null && nombreOriginal.length() > 245) {
                throw new IllegalArgumentException("El nombre del archivo del logo es demasiado largo (máximo 245 caracteres)");
            }
        }
    }
}