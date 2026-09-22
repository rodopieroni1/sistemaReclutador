package com.sistemaReclutador.sistemaReclutador.strategies.impl;

import org.springframework.stereotype.Component;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;
import com.sistemaReclutador.sistemaReclutador.strategies.EmpresaValidationStrategy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailUnicoValidationStrategy implements EmpresaValidationStrategy {

    private final EmpresaRepository empresaRepository;

    @Override
    public void validar(EmpresaRequest request, Empresa empresaExistente) {
        if (request.getEmail() == null) return;

        boolean emailCambio = empresaExistente == null || !empresaExistente.getEmail().equalsIgnoreCase(request.getEmail());
        if (emailCambio && empresaRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                empresaExistente == null ? "El email ya está registrado." : "El nuevo email ya se encuentra en uso."
            );
        }
    }
}