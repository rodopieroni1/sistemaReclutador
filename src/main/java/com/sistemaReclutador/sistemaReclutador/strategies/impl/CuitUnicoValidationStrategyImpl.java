package com.sistemaReclutador.sistemaReclutador.strategies.impl;

import org.springframework.stereotype.Component;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;
import com.sistemaReclutador.sistemaReclutador.strategies.EmpresaValidationStrategy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CuitUnicoValidationStrategyImpl implements EmpresaValidationStrategy {

    private final EmpresaRepository empresaRepository;

    @Override
    public void validar(EmpresaRequest request, Empresa empresaExistente) {
        if (request.getCuit() == null) return;

        boolean cuitCambio = empresaExistente == null || !empresaExistente.getCuit().equals(request.getCuit());
        if (cuitCambio && empresaRepository.existsByCuit(request.getCuit())) {
            throw new IllegalArgumentException(
                empresaExistente == null ? "El CUIT ya está registrado." : "El nuevo CUIT ya se encuentra en uso."
            );
        }
    }
}